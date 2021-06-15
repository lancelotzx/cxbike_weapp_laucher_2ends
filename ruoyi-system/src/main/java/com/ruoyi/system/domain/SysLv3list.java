package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 三级列表对象 sys_lv3list
 * 
 * @author ruoyi
 * @date 2021-06-05
 */
public class SysLv3list extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 使用自增id作为主键 */
    private Long id;

    /** 第二级图标的id */
    @Excel(name = "第二级图标的id")
    private Long iconid;

    /** 第三极列表子项名称 */
    @Excel(name = "第三极列表子项名称")
    private String name;

    /** 第三级列表子项价格 */
    @Excel(name = "第三级列表子项价格")
    private BigDecimal price;

    /** 第三级列表子项图片名称 */
    @Excel(name = "第三级列表子项图片名称")
    private String picname;

    /** 第三级列表子项图片url */
    @Excel(name = "第三级列表子项图片url")
    private String picurl;

    /** 第三级子项对应的链接类型，1小程序，2H5 */
    @Excel(name = "第三级子项对应的链接类型，1小程序，2H5")
    private String type;

    /** 子项对应小程序id，当为小程序时启用 */
    @Excel(name = "子项对应小程序id，当为小程序时启用")
    private String appid;

    /** 子项对应链接名称，可以为小程序名称或H5网站名称 */
    @Excel(name = "子项对应链接名称，可以为小程序名称或H5网站名称")
    private String linkname;

    /** 子项对应h5的地址，当为h5类型时启用 */
    @Excel(name = "子项对应h5的地址，当为h5类型时启用")
    private String h5url;

    /** 标签，逗号分隔，方便用户维护数据 */
    @Excel(name = "标签，逗号分隔，方便用户维护数据")
    private String tags;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setIconid(Long iconid) 
    {
        this.iconid = iconid;
    }

    public Long getIconid() 
    {
        return iconid;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setPicname(String picname) 
    {
        this.picname = picname;
    }

    public String getPicname() 
    {
        return picname;
    }
    public void setPicurl(String picurl) 
    {
        this.picurl = picurl;
    }

    public String getPicurl() 
    {
        return picurl;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setAppid(String appid) 
    {
        this.appid = appid;
    }

    public String getAppid() 
    {
        return appid;
    }
    public void setLinkname(String linkname) 
    {
        this.linkname = linkname;
    }

    public String getLinkname() 
    {
        return linkname;
    }
    public void setH5url(String h5url) 
    {
        this.h5url = h5url;
    }

    public String getH5url() 
    {
        return h5url;
    }
    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }

    public void cleanid() {this.id = null; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("iconid", getIconid())
            .append("name", getName())
            .append("price", getPrice())
            .append("picname", getPicname())
            .append("picurl", getPicurl())
            .append("type", getType())
            .append("appid", getAppid())
            .append("linkname", getLinkname())
            .append("h5url", getH5url())
            .append("tags", getTags())
            .toString();
    }
}
