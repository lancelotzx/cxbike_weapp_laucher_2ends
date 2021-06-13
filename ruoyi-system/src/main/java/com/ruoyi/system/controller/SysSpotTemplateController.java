package com.ruoyi.system.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysSpotTemplate;
import com.ruoyi.system.service.ISysSpotTemplateService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 模版管理Controller
 * 
 * @author RF
 * @date 2021-06-13
 */
@RestController
@RequestMapping("/system/template")
public class SysSpotTemplateController extends BaseController
{
    @Autowired
    private ISysSpotTemplateService sysSpotTemplateService;

    /**
     * 查询模版管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysSpotTemplate sysSpotTemplate)
    {
        startPage();
        List<SysSpotTemplate> list = sysSpotTemplateService.selectSysSpotTemplateList(sysSpotTemplate);
        return getDataTable(list);
    }

    /**
     * 导出模版管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:template:export')")
    @Log(title = "模版管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysSpotTemplate sysSpotTemplate)
    {
        List<SysSpotTemplate> list = sysSpotTemplateService.selectSysSpotTemplateList(sysSpotTemplate);
        ExcelUtil<SysSpotTemplate> util = new ExcelUtil<SysSpotTemplate>(SysSpotTemplate.class);
        return util.exportExcel(list, "模版管理数据");
    }

    /**
     * 获取模版管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysSpotTemplateService.selectSysSpotTemplateById(id));
    }

    /**
     * 新增模版管理
     */
    @PreAuthorize("@ss.hasPermi('system:template:add')")
    @Log(title = "模版管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSpotTemplate sysSpotTemplate)
    {
        // return toAjax(sysSpotTemplateService.insertSysSpotTemplate(sysSpotTemplate));
        int row = 0;
        try {
            row = sysSpotTemplateService.insertSysSpotTemplate(sysSpotTemplate);
            return toAjax(row);
        }catch (Exception e){
            return toAjax(0);
        }
    }

    /**
     * 修改模版管理
     */
    @PreAuthorize("@ss.hasPermi('system:template:edit')")
    @Log(title = "模版管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSpotTemplate sysSpotTemplate)
    {
       // return toAjax(sysSpotTemplateService.updateSysSpotTemplate(sysSpotTemplate));
        int row = 0;
        try {
            row = sysSpotTemplateService.updateSysSpotTemplate(sysSpotTemplate);
            return toAjax(row);
        }catch (Exception e){
            return toAjax(0);
        }
    }

    /**
     * 删除模版管理
     */
    @PreAuthorize("@ss.hasPermi('system:template:remove')")
    @Log(title = "模版管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysSpotTemplateService.deleteSysSpotTemplateByIds(ids));
    }
}
