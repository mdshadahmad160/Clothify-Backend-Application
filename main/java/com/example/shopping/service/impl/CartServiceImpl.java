package com.example.shopping.service.impl;

import com.example.shopping.appConstant.LoggingConstants;
import com.example.shopping.config.JwtTokenUtil;
import com.example.shopping.dto.AddToCartDto;
import com.example.shopping.dto.ProductQuantityDto;
import com.example.shopping.dto.ResponseDto;
import com.example.shopping.dto.ViewCartDto;
import com.example.shopping.entity.Cart;
import com.example.shopping.entity.Product;
import com.example.shopping.entity.ProductQuantity;
import com.example.shopping.entity.User;
import com.example.shopping.exception.NoSuchCartExists;
import com.example.shopping.exception.NoSuchProductExists;
import com.example.shopping.exception.NoSuchUserExists;
import com.example.shopping.exception.QuantityExceeded;
import com.example.shopping.repository.CartRepository;
import com.example.shopping.repository.ProductQuantityRepository;
import com.example.shopping.repository.ProductRepository;
import com.example.shopping.repository.UserRepository;
import com.example.shopping.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


import static com.example.shopping.appConstant.AppConstants.CART_ORDER_PENDING;
import static com.example.shopping.appConstant.AppConstants.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {


    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductQuantityRepository productQuantityRepository;

    @Value("${spring.application.responseCode}")
    private String responseCode;


    /**
     * First we need get cart from authToken
     * for this create a method
     */

    public Optional<Cart> getCartFromToken(String authToken) {
        String jwtToken = authToken.replace(TOKEN_PREFIX, "");
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        User user = userRepository.findUserByEmailId(username).get();
        return cartRepository.findCartByStatusAndUser(CART_ORDER_PENDING, user);
    }

    /**
     * @param authToken
     * @param addToCartDtos
     * @return
     */

    @Override
    public ResponseDto addToCart(String authToken, List<AddToCartDto> addToCartDtos) {
        var methodName = "CartServiceImpl:addToCart";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, authToken, addToCartDtos);
        String jwtToken = authToken.replace(TOKEN_PREFIX, "");
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        Optional<Cart> cart = getCartFromToken(authToken);
        User user = userRepository.findUserByEmailId(username).get();
        Cart newCart;
        Map<Long, ProductQuantity> productQuantityMap = null;
        if (cart.isPresent()) {
            newCart = cart.get();
            productQuantityMap = productQuantityRepository.findAllByCart(newCart).
                    stream().collect(Collectors.toMap(ProductQuantity::getProductId, Function.identity()));
        } else {
            newCart = new Cart();
            newCart.setUser(user);
            newCart.setStatus(CART_ORDER_PENDING);
        }
        List<ProductQuantity> productQuantities = new ArrayList<>();
        Map<Integer, Product> products = productRepository.
                findAllByProductIdIn(addToCartDtos.stream().map(AddToCartDto::getProductId)
                        .collect(Collectors.toList())).
                stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));
        List<Product> productList = new ArrayList<>();
        Map<Long, ProductQuantity> finalProductQuantityMap = productQuantityMap;
        addToCartDtos.forEach(addToCartDto -> {
            Product product = products.get(addToCartDto.getProductId());
            if (Objects.isNull(product)) {
                throw new NoSuchProductExists("Product with Product Id: " + addToCartDto.getProductId() + " does not exist");
            }
            if (product.getQuantity() <= addToCartDto.getQuantity()) {
                throw new QuantityExceeded("Product with Product Id: " + addToCartDto.getProductId() + " has only " + product.getQuantity() + " left");
            }
            product.setQuantity(product.getQuantity() - addToCartDto.getQuantity());
            ProductQuantity productQuantity;
            if (cart.isPresent() && finalProductQuantityMap.containsKey(addToCartDto.getProductId())) {
                productQuantity = finalProductQuantityMap.get(addToCartDto.getProductId());
                productQuantity.setQuantity(productQuantity.getQuantity() + addToCartDto.getQuantity());
            } else {
                productQuantity = new ProductQuantity();
                productQuantity.setProductId(addToCartDto.getProductId());
                productQuantity.setQuantity(addToCartDto.getQuantity());
            }
            newCart.setTotalPrice(newCart.getTotalPrice() + product.getPrice() * addToCartDto.getQuantity());
            productQuantity.setCart(newCart);
            productQuantities.add(productQuantity);
            productList.add(product);
        });
        productRepository.saveAll(productList);
        productQuantityRepository.saveAll(productQuantities);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);
        return new ResponseDto(responseCode, "Successfully added to cart");
    }

    /**
     * @param authToken
     * @return
     */

    @Override
    public ViewCartDto viewCart(String authToken) {
        String jwtToken = authToken.replace(TOKEN_PREFIX, "");
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        User user = userRepository.findUserByEmailId(username).orElseThrow(
                () -> new NoSuchUserExists("User Does not Exists:!!"));
        Optional<Cart> cart = cartRepository.findCartByStatusAndUser(CART_ORDER_PENDING, user);
        if (!cart.isPresent()) {
            throw new NoSuchCartExists("There are No Pending Cart!!!");
        }
        List<ProductQuantity> productQuantities = productQuantityRepository.findAllByCart(cart.get());
        List<Long> productIds = productQuantities.stream().map(
                ProductQuantity::getProductId).collect(Collectors.toList());
        Map<Integer, Product> productMap = productRepository.findAllByProductIdIn(productIds)
                .stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));


        ViewCartDto viewCartDto = new ViewCartDto();
        viewCartDto.setFirstName(user.getFirstName());
        viewCartDto.setLastName(user.getLastName());
        viewCartDto.setContactNumber(user.getContactNumber());
        viewCartDto.setCartStatus(cart.get().getStatus());
        viewCartDto.setTotalPrice(cart.get().getTotalPrice());

        List<ProductQuantityDto> productQuantityDtos = new ArrayList<>();
        productQuantities.forEach(productQuantity -> {
            ProductQuantityDto productQuantityDto = new ProductQuantityDto();
            productQuantityDto.setProductId(productQuantity.getProductQuantityId());
            productQuantityDto.setQuantity(productQuantity.getQuantity());
            productQuantityDto.setPrice(productMap.get(productQuantity.getProductId()).getPrice());

            productQuantityDto.setTotalPrice(productQuantity.getQuantity() * productMap.get(productQuantity.getProductId()).getPrice());
            productQuantityDtos.add(productQuantityDto);
        });
        viewCartDto.setProductQuantityDtos(productQuantityDtos);
        return viewCartDto;

    }

    @Override
    public ResponseDto updateCart(String authToken, List<AddToCartDto> addToCartDtos) {
        Optional<Cart> cart = getCartFromToken(authToken);
        if (!cart.isPresent()) {
            throw new NoSuchCartExists("There are No Pending Cart!!!!");
        }
        List<ProductQuantity> productQuantities = productQuantityRepository.findAllByCart(cart.get());
        Map<Long, ProductQuantity> productQuantityMap = productQuantities.stream()
                .collect(Collectors.toMap(ProductQuantity::getProductId, Function.identity()));
        List<Long> productIds = addToCartDtos.stream()
                .map(AddToCartDto::getProductId).collect(Collectors.toList());
        Map<Integer, Product> productMap = productRepository.findAllByProductIdIn(productIds)
                .stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));

        List<ProductQuantity> deletedProductQuantities = new ArrayList<>();
        List<Product> productList = new ArrayList<>();
        addToCartDtos.forEach(addToCartDto -> {
            ProductQuantity productQuantity = productQuantityMap.get(addToCartDto.getProductId());
            Product product = productMap.get(addToCartDto.getProductId());
            if (Objects.equals(addToCartDto.getQuantity(), 0)) {
                product.setQuantity(product.getQuantity() + productQuantity.getQuantity());
                cart.get().setTotalPrice(cart.get().getTotalPrice() - product.getPrice() * productQuantity.getQuantity());
                deletedProductQuantities.add(productQuantity);
            } else {
                product.setQuantity(product.getQuantity() + productQuantity.getQuantity() - addToCartDto.getQuantity());
                productQuantity.setQuantity(addToCartDto.getQuantity());
            }
            productList.add(product);
        });
        productQuantityRepository.deleteAll(deletedProductQuantities);
        productRepository.saveAll(productList);
        return new ResponseDto(responseCode, "Successfully Updated Cart!!!");
    }

    @Override
    public ResponseDto deleteCart(String authToken) {

        Optional<Cart> cart = getCartFromToken(authToken);
        if (!cart.isPresent()) {
            throw new NoSuchCartExists("There are no pending cart");
        }
        List<ProductQuantity> productQuantities = productQuantityRepository.findAllByCart(cart.get());
        Map<Integer, Product> productMap = productRepository.findAllByProductIdIn(productQuantities.stream()
                        .map(ProductQuantity::getProductId).collect(Collectors.toList())).
                stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));
        productQuantities.forEach(productQuantity -> {
            Product product = productMap.get(productQuantity.getProductId());
            product.setQuantity(product.getQuantity() + productQuantity.getQuantity());
        });
        productQuantityRepository.deleteAll(productQuantities);
        cartRepository.delete(cart.get());
        productRepository.saveAll(productMap.values());
        return new ResponseDto(responseCode, "Successfully deleted all the products in the cart");


    }
}
