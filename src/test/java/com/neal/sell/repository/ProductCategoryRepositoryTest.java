package com.neal.sell.repository;

import com.neal.sell.entity.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOneTest(){
        ProductCategory productCategory = productCategoryRepository.findById(1).get();
        System.out.println(productCategory.getCategoryName());
    }

    @Test
    public void updateOneTest(){
        ProductCategory productCategory = productCategoryRepository.findById(1).get();
        productCategory.setCategoryName("New Test");
        ProductCategory result = productCategoryRepository.save(productCategory);
        Assert.assertNotNull(result);
    }
}