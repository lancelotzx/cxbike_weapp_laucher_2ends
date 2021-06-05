package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysIcon;
import com.ruoyi.system.domain.SysLv3list;

/**
 * 图标Mapper接口
 * 
 * @author rf
 * @date 2021-06-05
 */
public interface SysIconMapper 
{
    /**
     * 查询图标
     * 
     * @param iconid 图标ID
     * @return 图标
     */
    public SysIcon selectSysIconById(Long iconid);

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
     * 删除图标
     * 
     * @param iconid 图标ID
     * @return 结果
     */
    public int deleteSysIconById(Long iconid);

    /**
     * 批量删除图标
     * 
     * @param iconids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysIconByIds(Long[] iconids);

    /**
     * 批量删除${subTable.functionName}
     * 
     * @param customerIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysLv3listByIconids(Long[] iconids);
    
    /**
     * 批量新增${subTable.functionName}
     * 
     * @param sysLv3listList ${subTable.functionName}列表
     * @return 结果
     */
    public int batchSysLv3list(List<SysLv3list> sysLv3listList);
    

    /**
     * 通过图标ID删除${subTable.functionName}信息
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteSysLv3listByIconid(Long iconid);
}
