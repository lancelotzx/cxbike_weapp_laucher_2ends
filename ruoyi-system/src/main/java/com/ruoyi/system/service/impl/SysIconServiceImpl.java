package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.domain.SysLv3list;
import com.ruoyi.system.mapper.SysIconMapper;
import com.ruoyi.system.domain.SysIcon;
import com.ruoyi.system.service.ISysIconService;

/**
 * 图标Service业务层处理
 * 
 * @author rf
 * @date 2021-06-05
 */
@Service
public class SysIconServiceImpl implements ISysIconService 
{
    @Autowired
    private SysIconMapper sysIconMapper;

    /**
     * 查询图标
     * 
     * @param iconid 图标ID
     * @return 图标
     */
    @Override
    public SysIcon selectSysIconById(Long iconid)
    {
        return sysIconMapper.selectSysIconById(iconid);
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
    @Transactional
    @Override
    public int insertSysIcon(SysIcon sysIcon)
    {
        int rows = sysIconMapper.insertSysIcon(sysIcon);
        insertSysLv3list(sysIcon);
        return rows;
    }

    /**
     * 修改图标
     * 
     * @param sysIcon 图标
     * @return 结果
     */
    @Transactional
    @Override
    public int updateSysIcon(SysIcon sysIcon)
    {
        sysIconMapper.deleteSysLv3listByIconid(sysIcon.getIconid());
        insertSysLv3list(sysIcon);
        return sysIconMapper.updateSysIcon(sysIcon);
    }

    /**
     * 批量删除图标
     * 
     * @param iconids 需要删除的图标ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSysIconByIds(Long[] iconids)
    {
        sysIconMapper.deleteSysLv3listByIconids(iconids);
        return sysIconMapper.deleteSysIconByIds(iconids);
    }

    /**
     * 删除图标信息
     * 
     * @param iconid 图标ID
     * @return 结果
     */
    @Override
    public int deleteSysIconById(Long iconid)
    {
        sysIconMapper.deleteSysLv3listByIconid(iconid);
        return sysIconMapper.deleteSysIconById(iconid);
    }

    /**
     * 新增${subTable.functionName}信息
     * 
     * @param sysIcon 图标对象
     */
    public void insertSysLv3list(SysIcon sysIcon)
    {
        List<SysLv3list> sysLv3listList = sysIcon.getSysLv3listList();
        Long iconid = sysIcon.getIconid();
        if (StringUtils.isNotNull(sysLv3listList))
        {
            List<SysLv3list> list = new ArrayList<SysLv3list>();
            for (SysLv3list sysLv3list : sysLv3listList)
            {
                sysLv3list.setIconid(iconid);
                list.add(sysLv3list);
            }
            if (list.size() > 0)
            {
                sysIconMapper.batchSysLv3list(list);
            }
        }
    }
}
