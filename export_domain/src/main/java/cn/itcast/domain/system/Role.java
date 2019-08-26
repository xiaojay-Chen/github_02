package cn.itcast.domain.system;

/**
 * Role实体类
 */
public class Role {
    private String id;
    private String name;
    private String remark;
    private Long orderNo;
    private String companyId;
    private String companyName;

    public Role() {
    }

    public Role(String id, String name, String remark, Long orderNo, String companyId, String companyName) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.orderNo = orderNo;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", orderNo=" + orderNo +
                ", companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                '}'+"\n";
    }
}
