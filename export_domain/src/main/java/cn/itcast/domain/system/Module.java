package cn.itcast.domain.system;

import java.util.Objects;

public class Module {

    private String id;
    //父模块id
    private String parentId;
    //父模块名称,冗余字段
    private String parentName;
    //模块名称
    private String name;
    private Integer layerNum;
    private Integer isLeaf;
    private String ico;
    private String cpermission;
    private String curl;
    //0 主菜单/1 左侧菜单/2按钮
    private Integer ctype;
    //1启用0停用
    private Integer state;
    /**
     * 从属关系
     *  0：sass系统内部菜单
     *  1：租用企业菜单
     */
    private Integer belong;
    private String cwhich;
    private Integer quoteNum;
    private String remark;
    private Integer orderNo;

    public Module() {
    }

    public Module(String id, String parentId, String parentName, String name, Integer layerNum, Integer isLeaf, String ico, String cpermission, String curl, Integer ctype, Integer state, Integer belong, String cwhich, Integer quoteNum, String remark, Integer orderNo) {
        this.id = id;
        this.parentId = parentId;
        this.parentName = parentName;
        this.name = name;
        this.layerNum = layerNum;
        this.isLeaf = isLeaf;
        this.ico = ico;
        this.cpermission = cpermission;
        this.curl = curl;
        this.ctype = ctype;
        this.state = state;
        this.belong = belong;
        this.cwhich = cwhich;
        this.quoteNum = quoteNum;
        this.remark = remark;
        this.orderNo = orderNo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){return true;}
        if (!(obj instanceof Module)){return false;}
        Module module = (Module) obj;
        return Objects.equals(id,module.id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLayerNum() {
        return layerNum;
    }

    public void setLayerNum(Integer layerNum) {
        this.layerNum = layerNum;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getCpermission() {
        return cpermission;
    }

    public void setCpermission(String cpermission) {
        this.cpermission = cpermission;
    }

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl;
    }

    public Integer getCtype() {
        return ctype;
    }

    public void setCtype(Integer ctype) {
        this.ctype = ctype;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getBelong() {
        return belong;
    }

    public void setBelong(Integer belong) {
        this.belong = belong;
    }

    public String getCwhich() {
        return cwhich;
    }

    public void setCwhich(String cwhich) {
        this.cwhich = cwhich;
    }

    public Integer getQuoteNum() {
        return quoteNum;
    }

    public void setQuoteNum(Integer quoteNum) {
        this.quoteNum = quoteNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentName='" + parentName + '\'' +
                ", name='" + name + '\'' +
                ", layerNum=" + layerNum +
                ", isLeaf=" + isLeaf +
                ", ico='" + ico + '\'' +
                ", cpermission='" + cpermission + '\'' +
                ", curl='" + curl + '\'' +
                ", ctype=" + ctype +
                ", state=" + state +
                ", belong=" + belong +
                ", cwhich='" + cwhich + '\'' +
                ", quoteNum=" + quoteNum +
                ", remark='" + remark + '\'' +
                ", orderNo=" + orderNo +
                '}'+"\n";
    }
}
