package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysLv3list;

/**
 * 三级列表Service接口
 * 
 * @author ruoyi
 * @date 2021-06-05
 */
public interface ISysLv3listService 
{
    /**
     * 查询三级列表
     * 
     * @param id 三级列表ID
     * @return 三级列表
     */
    public SysLv3list selectSysLv3listById(Long id);

    /**
     * 查询三级列表列表
     * 
     * @param sysLv3list 三级列表
     * @return 三级列表集合
     */
    public List<SysLv3list> selectSysLv3listList(SysLv3list sysLv3list);

    /**
     * 新增三级列表
     * 
     * @param sysLv3list 三级列表
     * @return 结果
     */
    public int insertSysLv3list(SysLv3list sysLv3list);

    /**
     * 修改三级列表
     * 
     * @param sysLv3list 三级列表
     * @return 结果
     */
    public int updateSysLv3list(SysLv3list sysLv3list);

    /**
     * 批量删除三级列表
     * 
     * @param ids 需要删除的三级列表ID
     * @return 结果
     */
    public int deleteSysLv3listByIds(Long[] ids);

    /**
     * 删除三级列表信息
     * 
     * @param id 三级列表ID
     * @return 结果
     */
    public int deleteSysLv3listById(Long id);
}
