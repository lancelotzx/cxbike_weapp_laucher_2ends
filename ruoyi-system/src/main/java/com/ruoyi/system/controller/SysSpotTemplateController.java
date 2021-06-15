package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.domain.SysIcon;
import com.ruoyi.system.domain.SysLv3list;
import com.ruoyi.system.domain.SysSpot;
import com.ruoyi.system.mapper.SysIconMapper;
import com.ruoyi.system.mapper.SysLv3listMapper;
import com.ruoyi.system.mapper.SysSpotMapper;
import com.ruoyi.system.service.ISysIconService;
import com.ruoyi.system.service.ISysLv3listService;
import com.ruoyi.system.service.ISysSpotService;
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

import javax.validation.constraints.Null;


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

    @Autowired
    private ISysSpotService sysSpotService;

    @Autowired
    private ISysIconService sysIconService;

    @Autowired
    private ISysLv3listService sysLv3listService;

    @Autowired
    private SysSpotMapper sysSpotMapper;

    @Autowired
    private SysIconMapper sysIconMapper;

    @Autowired
    private SysLv3listMapper sysLv3listMapper;

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
     * 通过模版增加数据管理
     */
    @PreAuthorize("@ss.hasPermi('system:template:add')")
    @Log(title = "模版生成数据", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult add2(@RequestBody SysSpotTemplate sysSpotTemplate)
    {
        boolean ret;
        int row = 0;
        System.out.println(sysSpotTemplate.getScenicid() + ',' + sysSpotTemplate.getTemplateName());
        // 这里进行数据处理

        ret = buildMultiDataFromTemplate(sysSpotTemplate.getScenicid(), sysSpotTemplate.getTemplateName());
        if(ret == true)
            return toAjax(1);
        else
            return toAjax(0);
    }

    // 批量生成数据的业务处理
    //Step1.先获取old景区的数据并插入到sys_spot表
    //Step2.再获取old景区的icon数据并插入到sys_icon表
    boolean buildMultiDataFromTemplate(String oldScenicId,String newSpotName){

        //Step1 Start 景区级别的复制
        SysSpot sysSpotNew =  sysSpotService.selectSysSpotById(oldScenicId);
        String newScenicId = sysSpotNew.getScenicid() + '_' + IdUtils.simpleUUID();
        sysSpotNew.setScenicid(newScenicId);
        sysSpotNew.setName(newSpotName);
        int row = 0;
        try {
            System.out.println("here");
            //Step1 Over
            //sysSpotService.insertSysSpot(sysSpotNew);
            sysSpotMapper.insertSysSpot(sysSpotNew);
            //Step2
            insertNewSysIcon(oldScenicId, newScenicId);

            return true;
        }catch (Exception e){
            return false;
        }
    }
    //从oldSpotId对应的spot中获取icons并填充到icon表中，icon的scenicid为newSpotId
    //注意，必须要新增到icon表中，id是自增的，不能指定。
    //注意，此处需要通过mybatis的feature获取到刚刚新增的iconid
    public void insertNewSysIcon(String oldScenicId, String newScenicId)
    {
        SysSpot sysSpot = sysSpotService.selectSysSpotById(oldScenicId);
        List<SysIcon> sysIconList = sysSpot.getSysIconList();
        String scenicid = newScenicId;
        if (StringUtils.isNotNull(sysIconList))
        {
            for (SysIcon sysIcon : sysIconList)
            {
                sysIcon.setScenicid(scenicid);
                long oldiconid = sysIcon.getIconid();
                sysIcon.cleanIconid();
                //sysIconService.insertSysIcon(sysIcon);
                sysIconMapper.insertSysIcon(sysIcon);
                //通过mybatis的feature获取到最后insert的自增iconid，用于后续lv3的新增
                Long iconid = sysIcon.getIconid();
                //System.out.println(iconid);
                insertNewSysLv3(oldiconid, iconid);
            }
        }
    }

    //从oldSpotId对应的spot中获取icons并填充到icon表中，由于从icon级别开始的id都是自动
    //因此参数只需要旧iconid即可
    public void insertNewSysLv3(Long oldIconId, Long newIconId)
    {
        SysIcon sysIcon = sysIconService.selectSysIconById(oldIconId);
        List<SysLv3list> sysLv3listList = sysIcon.getSysLv3listList();
        if (StringUtils.isNotNull(sysLv3listList))
        {
            for (SysLv3list sysLv3list : sysLv3listList)
            {
                //这里的iconid需要是之前2级生成的iconid
                sysLv3list.setIconid(newIconId);
                sysLv3list.cleanid();
                //sysIconService.insertSysIcon(sysIcon);
                sysLv3listMapper.insertSysLv3list(sysLv3list);
            }
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
