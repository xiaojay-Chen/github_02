package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.vo.ContractProductVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 购销合同货物模块
 */
public interface ContractProductService {

    /**
     * 分页查询
     * @param contractProductExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    PageInfo<ContractProduct> findByPage(
            ContractProductExample contractProductExample, int pageNum, int pageSize);

    /**
     * 查询所有
     */
    List<ContractProduct> findAll(ContractProductExample contractProductExample);

    /**
     * 根据id查询
     */
    ContractProduct findById(String id);

    /**
     * 新增
     */
    void save(ContractProduct contractProduct);

    /**
     * 修改
     */
    void update(ContractProduct contractProduct);

    /**
     * 删除部门
     */
    void delete(String id);

    /**
     * 出货表统计，根据船期查询
     * @param companyId 企业id
     * @param shipTime 船期
     * @return
     */
    List<ContractProductVo> findByShipTime(String companyId,String shipTime);

}











