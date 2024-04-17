package com.example.shopping.repository;

import com.example.shopping.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Integer> {

    Optional<Company> findCompanyByCompanyName(String companyName);

    List<Company> findCompanyByCompanyNameContainingIgnoreCase(String companyName);

}
