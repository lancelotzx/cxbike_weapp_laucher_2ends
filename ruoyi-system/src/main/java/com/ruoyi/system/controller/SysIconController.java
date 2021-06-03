package com.ruoyi.system.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.ruoyi.system.domain.SysIcon;
import com.ruoyi.system.service.ISysIconService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图标Controller
 * 
 * @author rf
 * @date 2021-06-02
 */
@Api("图标管理")
@RestController
@RequestMapping("/system/icon")
public class SysIconController extends BaseController
{
    @Autowired
    private ISysIconService sysIconService;

    /**
     * 查询图标列表
     */
    @ApiOperation("查询图标列表")
    @PreAuthorize("@ss.hasPermi('system:icon:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysIcon sysIcon)
    {
        startPage();
        List<SysIcon> list = sysIconService.selectSysIconList(sysIcon);
        return getDataTable(list);
    }

    /**
     * 导出图标列表
     */
    @ApiOperation("导出图标列表")
    @PreAuthorize("@ss.hasPermi('system:icon:export')")
    @Log(title = "图标", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysIcon sysIcon)
    {
        List<SysIcon> list = sysIconService.selectSysIconList(sysIcon);
        ExcelUtil<SysIcon> util = new ExcelUtil<SysIcon>(SysIcon.class);
        return util.exportExcel(list, "图标数据");
    }

    /**
     * 获取图标详细信息
     */
    @ApiOperation("获取图标详细信息")
    @PreAuthorize("@ss.hasPermi('system:icon:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysIconService.selectSysIconById(id));
    }

    /**
     * 新增图标
     */
    @ApiOperation("新增图标")
    @PreAuthorize("@ss.hasPermi('system:icon:add')")
    @Log(title = "图标", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysIcon sysIcon)
    {
        return toAjax(sysIconService.insertSysIcon(sysIcon));
    }

    /**
     * 修改图标
     */
    @ApiOperation("修改图标")
    @PreAuthorize("@ss.hasPermi('system:icon:edit')")
    @Log(title = "图标", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysIcon sysIcon)
    {
        return toAjax(sysIconService.updateSysIcon(sysIcon));
    }

    /**
     * 删除图标
     */
    @ApiOperation("删除图标")
    @PreAuthorize("@ss.hasPermi('system:icon:remove')")
    @Log(title = "图标", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysIconService.deleteSysIconByIds(ids));
    }
}
