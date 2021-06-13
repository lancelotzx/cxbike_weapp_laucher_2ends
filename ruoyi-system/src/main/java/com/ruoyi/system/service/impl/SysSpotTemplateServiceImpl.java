package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysSpotTemplateMapper;
import com.ruoyi.system.domain.SysSpotTemplate;
import com.ruoyi.system.service.ISysSpotTemplateService;

/**
 * 模版管理Service业务层处理
 * 
 * @author RF
 * @date 2021-06-13
 */
@Service
public class SysSpotTemplateServiceImpl implements ISysSpotTemplateService 
{
    @Autowired
    private SysSpotTemplateMapper sysSpotTemplateMapper;

    /**
     * 查询模版管理
     * 
     * @param id 模版管理ID
     * @return 模版管理
     */
    @Override
    public SysSpotTemplate selectSysSpotTemplateById(Long id)
    {
        return sysSpotTemplateMapper.selectSysSpotTemplateById(id);
    }

    /**
     * 查询模版管理列表
     * 
     * @param sysSpotTemplate 模版管理
     * @return 模版管理
     */
    @Override
    public List<SysSpotTemplate> selectSysSpotTemplateList(SysSpotTemplate sysSpotTemplate)
    {
        return sysSpotTemplateMapper.selectSysSpotTemplateList(sysSpotTemplate);
    }

    /**
     * 新增模版管理
     * 
     * @param sysSpotTemplate 模版管理
     * @return 结果
     */
    @Override
    public int insertSysSpotTemplate(SysSpotTemplate sysSpotTemplate)
    {
        return sysSpotTemplateMapper.insertSysSpotTemplate(sysSpotTemplate);
    }

    /**
     * 修改模版管理
     * 
     * @param sysSpotTemplate 模版管理
     * @return 结果
     */
    @Override
    public int updateSysSpotTemplate(SysSpotTemplate sysSpotTemplate)
    {
        return sysSpotTemplateMapper.updateSysSpotTemplate(sysSpotTemplate);
    }

    /**
     * 批量删除模版管理
     * 
     * @param ids 需要删除的模版管理ID
     * @return 结果
     */
    @Override
    public int deleteSysSpotTemplateByIds(Long[] ids)
    {
        return sysSpotTemplateMapper.deleteSysSpotTemplateByIds(ids);
    }

    /**
     * 删除模版管理信息
     * 
     * @param id 模版管理ID
     * @return 结果
     */
    @Override
    public int deleteSysSpotTemplateById(Long id)
    {
        return sysSpotTemplateMapper.deleteSysSpotTemplateById(id);
    }
}
