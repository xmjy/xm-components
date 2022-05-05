package indi.xm.component.libcommon.utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import indi.xm.component.libcommon.model.bo.PageBO;
import indi.xm.component.libcommon.model.vo.PagedGridResultVO;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: ehirejava-order
 * @Package: com.job51.ehire.ehirejavaorder.util
 * @ClassName: PageHelperUtil
 * @Author: albert.fang
 * @Description: 分页工具类
 * @Date: 2021/12/28 10:31
 */
public class PageHelperUtil {
    /**
     * 常量 0
     */
    public static final int ZERO = 0;

    /**
     * 查询全部数据时，默认第一页
     */
    public static final int ONE = 1;

    /**
     * 开启分页
     */
    public static void startPage(PageBO pageBO) {
        if (pageBO != null && !pageBO.getPageSize().equals(ZERO)) {
            PageHelper.startPage(pageBO.getCurPage(), pageBO.getPageSize());
        } else {
            // 查询所有的时候，不需要执行count查询
            PageHelper.startPage(ONE, ZERO, false, null, true);
        }
    }

    /**
     * 设置排序
     *
     * @param orderBy 排序命令
     */
    public static void orderBy(String orderBy) {
        PageHelper.orderBy(orderBy);
    }

    /**
     * 获取分页展示层对象
     *
     * @param preList pageHelper插件真正影响的结果集，用于计算总页数和总记录数
     * @param nowList 真正需要返回的结果集（限制：必须和preList的记录数保持一致，不然计算的页数和总记录数会有出入）
     * @return 分页展示层对象
     */
    public static <T> PagedGridResultVO<T> getPageGrid(List<?> preList, List<T> nowList, PageBO pageBO) {

        // 分页处理
        PageInfo<?> pageList = new PageInfo<>(preList);

        PagedGridResultVO<T> gridResult = new PagedGridResultVO<>();

        // 当前第几页
        gridResult.setCurPage(pageBO == null ? ONE : pageBO.getCurPage());

        // list 分页后的数据
        gridResult.setRows(nowList);

        // total 总页数
        gridResult.setTotalPage(pageList.getPages());

        // records 总记录数
        gridResult.setTotal(pageList.getTotal());

        if (pageBO != null) {
            pageBO.setTotalPage(pageList.getPages());
            pageBO.setTotal(pageList.getTotal());
        }

        return gridResult;

    }


    /**
     * 获取分页展示层对象
     *
     * @param list 受PageHelper影响的结果集，同时也是真正需要返回分页返回的结果集
     * @return 分页展示层对象
     */
    public static <T> PagedGridResultVO<T> getPageGrid(List<T> list, PageBO pageBO) {
        return getPageGrid(list, list, pageBO);
    }

    /**
     * 获取分页展示层对象
     *
     * @param list 受PageHelper影响的结果集，同时也是真正需要返回分页返回的结果集
     * @return 分页展示层对象
     */
    public static <T> PagedGridResultVO<T> getPageGrid(List<T> list) {
        return getPageGrid(list, list, null);
    }

    /**
     * 转换为PageBO
     *
     * @param curPage:
     * @param pageSize:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/17 10:10
     */
    public static PageBO transToPageBO(Integer curPage, Integer pageSize) {
        PageBO pageBO = new PageBO();
        if (curPage == null) {
            //默认第一页
            pageBO.setCurPage(1);
        } else {
            pageBO.setCurPage(curPage);
        }
        if (pageSize == null) {
            //默认一页显示20条
            pageBO.setPageSize(20);
        } else {
            pageBO.setPageSize(pageSize);
        }
        return pageBO;
    }

    /**
     * 内存分页
     *
     * @param list:
     * @param pageBO:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/17 10:07
     */
    private static <T> List<T> memoryPageHelper(List<T> list, PageBO pageBO) {
        Assert.notNull(pageBO, "pageBO 不能为空");
        Integer pageIndex = pageBO.getCurPage();
        Integer pageSize = pageBO.getPageSize();
        Assert.notNull(pageIndex, "pageIndex 不能为空");
        Assert.notNull(pageSize, "pageSize 不能为空");
        Assert.isTrue(pageIndex > 0, "pageIndex必须大于0");
        Assert.isTrue(pageSize >= 0, "pageSize必须大于或等于0");
        if (list == null || list.size() == 0) {
            pageBO.setTotalPage(0);
            pageBO.setTotal(0L);
            return new ArrayList<>();
        }

        //查询全部
        if (pageSize.equals(0)) {
            pageBO.setTotalPage(1);
            pageBO.setTotal((long) list.size());
            return list;
        } else {
            //分页查询
            int total = list.size();
            int totalPage = (total + pageSize - 1) / pageSize;
            int fromIndex = (pageIndex - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            if (fromIndex >= total) {
                return new ArrayList<>();
            }
            List<T> newList = list.subList(fromIndex, toIndex);
            pageBO.setTotal((long) total);
            pageBO.setTotalPage(totalPage);
            return newList;
        }
    }

    /**
     * 内存分页
     *
     * @param list:
     * @param curPage:
     * @param pageSize:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/17 10:42
     */
    public static <T> PagedGridResultVO<T> memoryPage(List<T> list, Integer curPage, Integer pageSize) {

        PageBO pageBO = transToPageBO(curPage, pageSize);
        List<T> newList = memoryPageHelper(list, pageBO);
        PagedGridResultVO<T> gridResult = new PagedGridResultVO<>();
        gridResult.setCurPage(pageBO.getCurPage());
        gridResult.setTotalPage(pageBO.getTotalPage());
        gridResult.setTotal(pageBO.getTotal());
        gridResult.setRows(newList);
        return gridResult;
    }


}
