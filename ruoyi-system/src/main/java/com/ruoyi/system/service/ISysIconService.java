package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysIcon;

/**
 * 图标Service接口
 * 
 * @author rf
 * @date 2021-06-02
 */
public interface ISysIconService 
{
    /**
     * 查询图标
     * 
     * @param id 图标ID
     * @return 图标
     */
    public SysIcon selectSysIconById(Long id);

    /**
     * 查询图标列表
     * 
     * @param sysIcon 图标
     * @return 图标集合
     */
    public List<SysIcon> selectSysIconList(SysIcon sysIcon);

    /**
     * 新增图标
     * 
     * @param sysIcon 图标
     * @return 结果
     */
    public int insertSysIcon(SysIcon sysIcon);

    /**
     * 修改图标
     * 
     * @param sysIcon 图标
     * @return 结果
     */
    public int updateSysIcon(SysIcon sysIcon);

    /**
     * 批量删除图标
     * 
     * @param ids 需要删除的图标ID
     * @return 结果
     */
    public int deleteSysIconByIds(Long[] ids);

    /**
     * 删除图标信息
     * 
     * @param id 图标ID
     * @return 结果
     */
    public int deleteSysIconById(Long id);
}
