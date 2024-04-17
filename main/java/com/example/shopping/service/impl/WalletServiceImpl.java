package com.example.shopping.service.impl;

import com.example.shopping.config.JwtTokenUtil;
import com.example.shopping.dto.ResponseDto;
import com.example.shopping.entity.User;
import com.example.shopping.entity.Wallet;
import com.example.shopping.exception.NoSuchUserExists;
import com.example.shopping.repository.UserRepository;
import com.example.shopping.repository.WalletRepository;
import com.example.shopping.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.shopping.appConstant.AppConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {


    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public String getUsernameFromAuthToken(String authToken) {
        return jwtTokenUtil.getUsernameFromToken(authToken.replace(TOKEN_PREFIX, ""));
    }

    @Override
    public ResponseDto addWallet(String authToken, String walletType, double walletBalance) {
        User user = userRepository.findUserByEmailId(getUsernameFromAuthToken(authToken)).orElseThrow(
                () -> new NoSuchUserExists("User with email id " + getUsernameFromAuthToken(authToken) + " does not exist")
        );
        Optional<Wallet> wallet = walletRepository.findWalletByWalletTypeAndUser(walletType, user);
        Wallet newWallet;
        if (wallet.isPresent()) {
            newWallet = wallet.get();
            newWallet.setWalletBalance(wallet.get().getWalletBalance() + walletBalance);
        } else {
            newWallet = new Wallet();
            switch (walletType) {
                case PAYTM:
                    newWallet.setWalletId(user.getContactNumber() + "@paytm");
                    break;
                case G_PAY:
                    newWallet.setWalletId(user.getContactNumber() + "@okicici");
                    break;
                case PHONE_PAY:
                    newWallet.setWalletId(user.getContactNumber() + "@ybl");
            }
            newWallet.setWalletType(walletType);
            newWallet.setWalletBalance(walletBalance);
            newWallet.setUser(user);
        }
        walletRepository.save(newWallet);
        return new ResponseDto("200", "Wallet added successfully");
    }
}

