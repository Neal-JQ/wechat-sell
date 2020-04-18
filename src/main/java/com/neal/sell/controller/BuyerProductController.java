package com.neal.sell.controller;

import com.neal.sell.entity.ProductCategory;
import com.neal.sell.entity.ProductInfo;
import com.neal.sell.service.ProductCategoryService;
import com.neal.sell.service.ProductInfoService;
import com.neal.sell.utils.ResultVoUtil;
import com.neal.sell.vo.ProductInfoVO;
import com.neal.sell.vo.ProductVO;
import com.neal.sell.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product/")
public class BuyerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    public ResultVO list(){
        // 查询所有上架商品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();
        // 查询Category
        List<Integer> productCategoryTypeList = productInfoList.stream().map(ProductInfo::getCategoryType).collect(Collectors.toList());
        List<ProductCategory> productCategoryList = productCategoryService.findByCategoryTypeIn(productCategoryTypeList);

        List<ProductVO> productVOs = new ArrayList<>();
        for(ProductCategory productCategory : productCategoryList){
            ProductVO productVo = new ProductVO();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVO> productInfoVOs = new ArrayList<>();
            for(ProductInfo productInfo: productInfoList){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVo = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVo);
                    productInfoVOs.add(productInfoVo);
                }
            }
            productVo.setProductInfoVOList(productInfoVOs);
            productVOs.add(productVo);
        }
        return ResultVoUtil.success(productVOs);
    }
}
