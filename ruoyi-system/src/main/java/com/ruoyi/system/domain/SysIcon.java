package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图标对象 sys_icon
 * 
 * @author rf
 * @date 2021-06-02
 */
public class SysIcon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图标id */
    private Long id;

    /** 景区id */
    @Excel(name = "景区id")
    private Long spotId;

    /** 图标名称 */
    @Excel(name = "图标名称")
    private String name;

    /** 图标链接地址 */
    @Excel(name = "图标链接地址")
    private String url;

    /** 链接类型，可能为小程序，列表，h5 */
    @Excel(name = "链接类型，可能为小程序，列表，h5")
    private String type;

    /** 标签，逗号分隔 */
    @Excel(name = "标签，逗号分隔")
    private String tags;

    /** 图标对应小程序id */
    @Excel(name = "图标对应小程序id")
    private String appid;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSpotId(Long spotId) 
    {
        this.spotId = spotId;
    }

    public Long getSpotId() 
    {
        return spotId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }
    public void setAppid(String appid) 
    {
        this.appid = appid;
    }

    public String getAppid() 
    {
        return appid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("spotId", getSpotId())
            .append("name", getName())
            .append("url", getUrl())
            .append("type", getType())
            .append("tags", getTags())
            .append("appid", getAppid())
            .toString();
    }
}
