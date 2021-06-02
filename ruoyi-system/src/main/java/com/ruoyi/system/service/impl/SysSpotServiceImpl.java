package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.domain.SysIcon;
import com.ruoyi.system.mapper.SysSpotMapper;
import com.ruoyi.system.domain.SysSpot;
import com.ruoyi.system.service.ISysSpotService;

/**
 * 景区Service业务层处理
 * 
 * @author rf
 * @date 2021-06-02
 */
@Service
public class SysSpotServiceImpl implements ISysSpotService 
{
    @Autowired
    private SysSpotMapper sysSpotMapper;

    /**
     * 查询景区
     * 
     * @param id 景区ID
     * @return 景区
     */
    @Override
    public SysSpot selectSysSpotById(Long id)
    {
        return sysSpotMapper.selectSysSpotById(id);
    }

    /**
     * 查询景区列表
     * 
     * @param sysSpot 景区
     * @return 景区
     */
    @Override
    public List<SysSpot> selectSysSpotList(SysSpot sysSpot)
    {
        return sysSpotMapper.selectSysSpotList(sysSpot);
    }

    /**
     * 新增景区
     * 
     * @param sysSpot 景区
     * @return 结果
     */
    @Transactional
    @Override
    public int insertSysSpot(SysSpot sysSpot)
    {
        int rows = sysSpotMapper.insertSysSpot(sysSpot);
        insertSysIcon(sysSpot);
        return rows;
    }

    /**
     * 修改景区
     * 
     * @param sysSpot 景区
     * @return 结果
     */
    @Transactional
    @Override
    public int updateSysSpot(SysSpot sysSpot)
    {
        sysSpotMapper.deleteSysIconBySpotId(sysSpot.getId());
        insertSysIcon(sysSpot);
        return sysSpotMapper.updateSysSpot(sysSpot);
    }

    /**
     * 批量删除景区
     * 
     * @param ids 需要删除的景区ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSysSpotByIds(Long[] ids)
    {
        sysSpotMapper.deleteSysIconBySpotIds(ids);
        return sysSpotMapper.deleteSysSpotByIds(ids);
    }

    /**
     * 删除景区信息
     * 
     * @param id 景区ID
     * @return 结果
     */
    @Override
    public int deleteSysSpotById(Long id)
    {
        sysSpotMapper.deleteSysIconBySpotId(id);
        return sysSpotMapper.deleteSysSpotById(id);
    }

    /**
     * 新增图标信息
     * 
     * @param sysSpot 景区对象
     */
    public void insertSysIcon(SysSpot sysSpot)
    {
        List<SysIcon> sysIconList = sysSpot.getSysIconList();
        Long id = sysSpot.getId();
        if (StringUtils.isNotNull(sysIconList))
        {
            List<SysIcon> list = new ArrayList<SysIcon>();
            for (SysIcon sysIcon : sysIconList)
            {
                sysIcon.setSpotId(id);
                list.add(sysIcon);
            }
            if (list.size() > 0)
            {
                sysSpotMapper.batchSysIcon(list);
            }
        }
    }
}
