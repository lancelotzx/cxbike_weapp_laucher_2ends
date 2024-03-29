package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysSpot;
import com.ruoyi.system.domain.SysIcon;

/**
 * 景区Mapper接口
 * 
 * @author rf
 * @date 2021-06-05
 */
public interface SysSpotMapper 
{
    /**
     * 查询景区
     * 
     * @param scenicid 景区ID
     * @return 景区
     */
    public SysSpot selectSysSpotById(String scenicid);

    /**
     * 查询景区列表
     * 
     * @param sysSpot 景区
     * @return 景区集合
     */
    public List<SysSpot> selectSysSpotList(SysSpot sysSpot);

    /**
     * 新增景区
     * 
     * @param sysSpot 景区
     * @return 结果
     */
    public int insertSysSpot(SysSpot sysSpot);

    /**
     * 修改景区
     * 
     * @param sysSpot 景区
     * @return 结果
     */
    public int updateSysSpot(SysSpot sysSpot);

    /**
     * 删除景区
     * 
     * @param scenicid 景区ID
     * @return 结果
     */
    public int deleteSysSpotById(String scenicid);

    /**
     * 批量删除景区
     * 
     * @param scenicids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysSpotByIds(String[] scenicids);

    /**
     * 批量删除图标
     * 
     * @param customerIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysIconByScenicIds(String[] scenicids);
    
    /**
     * 批量新增图标
     * 
     * @param sysIconList 图标列表
     * @return 结果
     */
    public int batchSysIcon(List<SysIcon> sysIconList);
    

    /**
     * 通过景区ID删除图标信息
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteSysIconByScenicId(String scenicid);
}
