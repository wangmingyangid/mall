package org.wmy.mall.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wmy.mall.constant.MallConstant;
import org.wmy.mall.dao.CategoryMapper;
import org.wmy.mall.pojo.Category;
import org.wmy.mall.service.ICategoryService;
import org.wmy.mall.vo.CategoryVo;
import org.wmy.mall.vo.ResponseVo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.wmy.mall.constant.MallConstant.ROOT_PARENT_ID;

/**
 * @author wmy
 * @create 2020-11-08 9:35
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {

        //1.从数据库查出所有种类
        List<Category> categories = categoryMapper.selectAll();

        /*//lambda+stream
        List<CategoryVo> categoryVoList1 = categories
                .stream()
                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID))
                .map(this::category2CategoryVo)
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                .collect(Collectors.toList());*/

        //2. 查出parent_id=0，放入categoryVoList
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categories) {
            if (category.getParentId().equals(ROOT_PARENT_ID)) {
                CategoryVo categoryVo = category2CategoryVo(category);
                categoryVoList.add(categoryVo);
            }
        }
        //放入之前先排个序
        categoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
        //3. 查出子目录
        findSubCategory(categoryVoList, categories);

        return ResponseVo.success(categoryVoList);
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id, resultSet, categories);
    }

    private void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for (Category category : categories) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }

    private CategoryVo category2CategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    /**
     * @param categoryVoList
     * @param categories     根据categoryVoList中所有元素的子类
     */
    private void findSubCategory(List<CategoryVo> categoryVoList, List<Category> categories) {
        if (categoryVoList == null) return;
        for (CategoryVo categoryVo : categoryVoList) {
            List<CategoryVo> subCategoryVoList = new ArrayList<>();
            for (Category category : categories) {
                if (categoryVo.getId().equals(category.getParentId())) {
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }
            }
            //放入之前先根据sort_order排个序
            subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
            categoryVo.setSubCategories(subCategoryVoList);
            findSubCategory(subCategoryVoList, categories);
        }
    }
}
