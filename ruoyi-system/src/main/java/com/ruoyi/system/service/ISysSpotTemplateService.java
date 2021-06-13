package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysSpotTemplate;

/**
 * 模版管理Service接口
 * 
 * @author RF
 * @date 2021-06-13
 */
public interface ISysSpotTemplateService 
{
    /**
     * 查询模版管理
     * 
     * @param id 模版管理ID
     * @return 模版管理
     */
    public SysSpotTemplate selectSysSpotTemplateById(Long id);

    /**
     * 查询模版管理列表
     * 
     * @param sysSpotTemplate 模版管理
     * @return 模版管理集合
     */
    public List<SysSpotTemplate> selectSysSpotTemplateList(SysSpotTemplate sysSpotTemplate);

    /**
     * 新增模版管理
     * 
     * @param sysSpotTemplate 模版管理
     * @return 结果
     */
    public int insertSysSpotTemplate(SysSpotTemplate sysSpotTemplate);

    /**
     * 修改模版管理
     * 
     * @param sysSpotTemplate 模版管理
     * @return 结果
     */
    public int updateSysSpotTemplate(SysSpotTemplate sysSpotTemplate);

    /**
     * 批量删除模版管理
     * 
     * @param ids 需要删除的模版管理ID
     * @return 结果
     */
    public int deleteSysSpotTemplateByIds(Long[] ids);

    /**
     * 删除模版管理信息
     * 
     * @param id 模版管理ID
     * @return 结果
     */
    public int deleteSysSpotTemplateById(Long id);
}
