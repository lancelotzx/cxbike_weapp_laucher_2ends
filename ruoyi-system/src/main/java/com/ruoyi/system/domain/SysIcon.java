package com.ruoyi.system.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图标对象 sys_icon
 * 
 * @author rf
 * @date 2021-06-05
 */
public class SysIcon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图标id,由于排序需要，使用自增id */
    private Long iconid;

    /** 景区id，使用uuid */
    @Excel(name = "景区id，使用uuid")
    private String scenicid;

    /** 图标名称 */
    @Excel(name = "图标名称")
    private String iconname;

    /** 图标图片链接地址 */
    @Excel(name = "图标图片链接地址")
    private String iconurl;

    /** 链接类型，可能为小程序，h5， 列表 */
    @Excel(name = "链接类型，可能为小程序，h5， 列表")
    private String type;

    /** 标签，逗号分隔，方便用户维护数据 */
    @Excel(name = "标签，逗号分隔，方便用户维护数据")
    private String tags;

    /** 图标对应小程序id，当为小程序时启用 */
    @Excel(name = "图标对应小程序id，当为小程序时启用")
    private String appid;

    /** 图标对应H5网址链接名称，或小程序名称 */
    @Excel(name = "图标对应H5网址链接名称，或小程序名称")
    private String linkname;

    /** 图标对应h5的地址，当为h5类型时启用 */
    @Excel(name = "图标对应h5的地址，当为h5类型时启用")
    private String h5url;

    /** $table.subTable.functionName信息 */
    private List<SysLv3list> sysLv3listList;

    public void setIconid(Long iconid) 
    {
        this.iconid = iconid;
    }

    public void cleanIconid()
    {
        this.iconid = null;
    }

    public Long getIconid() 
    {
        return iconid;
    }
    public void setScenicid(String scenicid) 
    {
        this.scenicid = scenicid;
    }

    public String getScenicid() 
    {
        return scenicid;
    }
    public void setIconname(String iconname) 
    {
        this.iconname = iconname;
    }

    public String getIconname() 
    {
        return iconname;
    }
    public void setIconurl(String iconurl) 
    {
        this.iconurl = iconurl;
    }

    public String getIconurl() 
    {
        return iconurl;
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

    public List<SysLv3list> getSysLv3listList()
    {
        return sysLv3listList;
    }

    public void setSysLv3listList(List<SysLv3list> sysLv3listList)
    {
        this.sysLv3listList = sysLv3listList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("iconid", getIconid())
            .append("scenicid", getScenicid())
            .append("iconname", getIconname())
            .append("iconurl", getIconurl())
            .append("type", getType())
            .append("tags", getTags())
            .append("appid", getAppid())
            .append("linkname", getLinkname())
            .append("h5url", getH5url())
            .append("sysLv3listList", getSysLv3listList())
            .toString();
    }
}
