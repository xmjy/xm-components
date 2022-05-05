package indi.xm.component.libcommon.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: ehirejava-order
 * @Package: com.job51.ehire.ehirejavaorder.model.bo
 * @ClassName: PageBO
 * @Author: albert.fang
 * @Description: 分页属性对象
 * @Date: 2021/12/30 10:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageBO {

    /**
     * 当前页数 默认 null 表示第一页
     */
    private Integer curPage;

    /**
     * 每页显示几条数据 默认 null 表示不分页，查询全部
     */
    private Integer pageSize;

    /**
     * 如果是分页的话，总页数就不为 null
     */
    private Integer totalPage;

    /**
     * 如果是分页的话，总记录数就不为 null
     */
    private Long total;

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageSize() {
        return pageSize == null ? 0 : pageSize;
    }

    public Integer getCurPage() {
        return curPage == null ? 1 : curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }
}
