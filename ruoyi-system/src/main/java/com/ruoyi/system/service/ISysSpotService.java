package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysSpot;

/**
 * 景区Service接口
 * 
 * @author rf
 * @date 2021-06-02
 */
public interface ISysSpotService 
{
    /**
     * 查询景区
     * 
     * @param id 景区ID
     * @return 景区
     */
    public SysSpot selectSysSpotById(Long id);

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
     * 批量删除景区
     * 
     * @param ids 需要删除的景区ID
     * @return 结果
     */
    public int deleteSysSpotByIds(Long[] ids);

    /**
     * 删除景区信息
     * 
     * @param id 景区ID
     * @return 结果
     */
    public int deleteSysSpotById(Long id);
}
