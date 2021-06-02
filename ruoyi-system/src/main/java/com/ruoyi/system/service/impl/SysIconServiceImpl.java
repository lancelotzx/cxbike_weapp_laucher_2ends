package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysIconMapper;
import com.ruoyi.system.domain.SysIcon;
import com.ruoyi.system.service.ISysIconService;

/**
 * 图标Service业务层处理
 * 
 * @author rf
 * @date 2021-06-02
 */
@Service
public class SysIconServiceImpl implements ISysIconService 
{
    @Autowired
    private SysIconMapper sysIconMapper;

    /**
     * 查询图标
     * 
     * @param id 图标ID
     * @return 图标
     */
    @Override
    public SysIcon selectSysIconById(Long id)
    {
        return sysIconMapper.selectSysIconById(id);
    }

    /**
     * 查询图标列表
     * 
     * @param sysIcon 图标
     * @return 图标
     */
    @Override
    public List<SysIcon> selectSysIconList(SysIcon sysIcon)
    {
        return sysIconMapper.selectSysIconList(sysIcon);
    }

    /**
     * 新增图标
     * 
     * @param sysIcon 图标
     * @return 结果
     */
    @Override
    public int insertSysIcon(SysIcon sysIcon)
    {
        return sysIconMapper.insertSysIcon(sysIcon);
    }

    /**
     * 修改图标
     * 
     * @param sysIcon 图标
     * @return 结果
     */
    @Override
    public int updateSysIcon(SysIcon sysIcon)
    {
        return sysIconMapper.updateSysIcon(sysIcon);
    }

    /**
     * 批量删除图标
     * 
     * @param ids 需要删除的图标ID
     * @return 结果
     */
    @Override
    public int deleteSysIconByIds(Long[] ids)
    {
        return sysIconMapper.deleteSysIconByIds(ids);
    }

    /**
     * 删除图标信息
     * 
     * @param id 图标ID
     * @return 结果
     */
    @Override
    public int deleteSysIconById(Long id)
    {
        return sysIconMapper.deleteSysIconById(id);
    }
}
