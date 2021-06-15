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
import com.ruoyi.system.domain.SysLv3list;
import com.ruoyi.system.service.ISysLv3listService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 三级列表Controller
 * 
 * @author ruoyi
 * @date 2021-06-05
 */
@Api("三级列表管理")
@RestController
@RequestMapping("/system/lv3list")
public class SysLv3listController extends BaseController
{
    @Autowired
    private ISysLv3listService sysLv3listService;

    /**
     * 查询三级列表列表
     */
    @ApiOperation("查询三级列表")
    @PreAuthorize("@ss.hasPermi('system:lv3list:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLv3list sysLv3list)
    {
        startPage();
        List<SysLv3list> list = sysLv3listService.selectSysLv3listList(sysLv3list);
        return getDataTable(list);
    }

    /**
     * 导出三级列表列表
     */
    @ApiOperation("导出三级列表数据")
    @PreAuthorize("@ss.hasPermi('system:lv3list:export')")
    @Log(title = "三级列表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysLv3list sysLv3list)
    {
        List<SysLv3list> list = sysLv3listService.selectSysLv3listList(sysLv3list);
        ExcelUtil<SysLv3list> util = new ExcelUtil<SysLv3list>(SysLv3list.class);
        return util.exportExcel(list, "三级列表数据");
    }

    /**
     * 获取三级列表详细信息
     */
    @ApiOperation("获取某id的三级列表详细信息")
    @PreAuthorize("@ss.hasPermi('system:lv3list:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysLv3listService.selectSysLv3listById(id));
    }

    /**
     * 新增三级列表
     */
    @ApiOperation("新增三级列表")
    @PreAuthorize("@ss.hasPermi('system:lv3list:add')")
    @Log(title = "三级列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLv3list sysLv3list)
    {
        return toAjax(sysLv3listService.insertSysLv3list(sysLv3list));
    }

    /**
     * 修改三级列表
     */
    @ApiOperation("修改三级列表")
    @PreAuthorize("@ss.hasPermi('system:lv3list:edit')")
    @Log(title = "三级列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLv3list sysLv3list)
    {
        return toAjax(sysLv3listService.updateSysLv3list(sysLv3list));
    }

    /**
     * 删除三级列表
     */
    @ApiOperation("删除三级列表数据")
    @PreAuthorize("@ss.hasPermi('system:lv3list:remove')")
    @Log(title = "三级列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysLv3listService.deleteSysLv3listByIds(ids));
    }
}
