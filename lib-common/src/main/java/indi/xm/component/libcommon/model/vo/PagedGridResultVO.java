package indi.xm.component.libcommon.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName: ehirejava-order
 * @Package: com.job51.ehire.ehirejavaorder.model.vo
 * @ClassName: PagedGridResultVO
 * @Author: albert.fang
 * @Description: 分页实体
 * @Date: 2021/12/28 10:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedGridResultVO<T> {

    /**
     * 当前页数
     */
    private Integer curPage;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 每行显示的内容
     */
    private List<T> rows;
}
