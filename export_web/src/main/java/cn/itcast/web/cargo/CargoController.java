package cn.itcast.web.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 控制器
 */
@Controller
@RequestMapping("/cargo/contract")
public class CargoController extends BaseController{

    /**
     * 1.注入购销合同服务接口
     */
    @Reference
    private ContractService contractService;

    /**
     * 列表分页查询
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1")int pageNum,@RequestParam(defaultValue = "6") int pageSize){

        // 构造查询条件
        ContractExample example = new ContractExample();
        // 根据create_time进行降序
        example.setOrderByClause("create_time desc");

        // 查询条件对象
        ContractExample.Criteria criteria = example.createCriteria();
        // 查询条件：企业id
        criteria.andCompanyIdEqualTo(getLoginCompanyId());

        /**
         * 细粒度权限控制，根据用户的degree等级判断，不同的级别显示不同的购销合同数据
         * degree  级别
         *      0-saas管理员
         *      1-管理所有下属部门和人员
         *      2-管理本部门
         *      3-普通员工
         */
        /* 获得当前登录用户 */
        User user = getLoginInfo();
        if (user.getDegree() == 3){
            // 说明是个普通员工：只能查询自己创建的购销合同
            criteria.andCreateByEqualTo(user.getId());
        }else if (user.getDegree() == 2){
            // 说明是部门经理，可以看到本部门下所有员工创建的购销合同
            criteria.andCreateDeptEqualTo(user.getDeptId());
        }else if (user.getDegree() == 1){
            // 说明可以查看其所有下属（子孙）部门和人员创建的购销合同
            PageInfo<Contract> pageInfo = contractService.selectByDeptId(user.getDeptId(), pageNum, pageSize);

            // 保存到会话域
            request.setAttribute("pageInfo",pageInfo);
            // 跳转到对象的页面
            return "/cargo/contract/contract-list";
        }

        // 调用service分页查询
        PageInfo<Contract> pageInfo = contractService.findByPage(example, pageNum, pageSize);


        // 保存到会话域
        request.setAttribute("pageInfo",pageInfo);
        // 跳转到对象的页面
        return "/cargo/contract/contract-list";
    }

    /**
     * 2.进入新增页面
     * 按钮方法名：/cargo/contract/toAdd.do
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(){

        // 直接给按钮返回新增页面路径
        return "/cargo/contract/contract-add";
    }

    /**
     * 3.进入修改页面
     * 按钮方法：/cargo/contract/toUpdate.do?id
     * @param id 企业id
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        // 根据企业id查询对象
        Contract contract = contractService.findById(id);
        // 保存修改数据
        request.setAttribute("contract",contract);

        // 跳转
        return "/cargo/contract/contract-update";
    }

    /**
     * 添加/修改
     * @param contract
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Contract contract){

        // 设置企业id 名称
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());

        // 根据有没有id判断是添加还是修改
        if (StringUtils.isEmpty(contract.getId())){
            /* 细粒度的权限控制 */
            /* 1.设置创建者 */
            contract.setCreateBy(getLoginInfo().getId());
            /* 2.设置创建者所属部门 */
            contract.setCreateDept(getLoginInfo().getDeptId());

            // 没有id。添加
            contractService.save(contract);

        }else {
            // 否则修改
            contractService.update(contract);

        }

        // 跳转回分页列表
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 根据购销合同id删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id){

        // 直接电泳方法删除
        contractService.delete(id);
        // 跳转回分页列表
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 购销合同：查看，提交，取消
     * 查看地址：http://localhost:8080/cargo/contract/toView.do
     * 提交地址：http://localhost:8080/cargo/contract/submit.do
     * 取消地址：http://localhost:8080/cargo/contract/cancel.do
     * @Param id:购销合同id
     */
    @RequestMapping("/toView")
    public String toView(String id){
        Contract contract = contractService.findById(id);
        request.setAttribute("contract",contract);

        return "/cargo/contract/contract-view";
    }

    @RequestMapping("/submit")
    public String submit(String id){
        Contract contract = contractService.findById(id);

        // 提交：状态修改为1
        contract.setId(id);
        contract.setState(1);
        contractService.update(contract);

        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping("/cancel")
    public String cancel(String id){
        Contract contract = new Contract();
        // 取消：状态修改为0
        contract.setId(id);
        contract.setState(0);
        contractService.update(contract);

        return "redirect:/cargo/contract/list.do";
    }
}
