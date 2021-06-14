<!--
20210614 wangjia：增加和修改景区模版时，用户选择的景区为下拉。
20210614 wangjia: 开始进行batchadd功能，在模版item中增加。
-->
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模板名称" prop="templateName">
        <el-input
          v-model="queryParams.templateName"
          placeholder="请输入模板名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:template:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:template:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:template:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:template:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模板名称" align="center" prop="templateName" />
      <el-table-column label="对应景区" align="center" prop="scenicid"  :formatter="formatter"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:template:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:template:remove']"
          >删除</el-button>
           <el-button
            size="mini"
            type="text"
            icon="el-icon-folder-add"
            @click="handleBatchAdd(scope.row)"
            v-hasPermi="['system:template:remove']"
          >数据生成</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改模版管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模板名称" prop="templateName">
          <el-input v-model="form.templateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="对应景区" prop="scenicid">
          <el-select v-model="form.scenicid" placeholder="对应景区">
            <el-option
              v-for="item in spotList"
              :key="item.scenicid"
              :label="item.name"
              :value="item.scenicid">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

     <!-- 生成批量数据对话框 -->
    <el-dialog title="通过模版生成" :visible.sync="open2" width="500px" append-to-body>
      <el-form ref="batchform" :model="batchform" :rules="batchrules" label-width="80px">
        <el-alert
          title="批量生成说明"
          type="success"
          description="批量生成数据完全复制模版数据，请记下当前输入的新景区名称，用于在景区、图标
          模块中查询当前生成的数据。景区名称不可重复。">
        </el-alert>
        
        <el-form-item label="景区名称" prop="newspotname">
          <el-input v-model="batchform.newspotname" placeholder="请输入新建景区名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBatchForm">确 定</el-button>
        <el-button @click="cancelBatch">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTemplate, getTemplate, delTemplate, addTemplate, updateTemplate, exportTemplate } from "@/api/system/template";
import { listSpot } from "@/api/system/spot";

export default {
  name: "Template",
  components: {
  },
  data() {
    return {
      spotTotal: 0,
      spotList:[],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 模版管理表格数据
      templateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      open2: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        templateName: null,
        scenicid: null
      },
      // 表单参数
      form: {},
      // 批量生成景区数据的form
      batchform: {},
      // 表单校验
      rules: {
        templateName: [
          { required: true, message: "模板名称不能为空", trigger: "blur" }
        ],
        scenicid: [
          { required: true, message: "外键对应景区spot表的主键id不能为空", trigger: "blur" }
        ]
      },
      batchrules: {
        newspotname: [
          { required: true, message: "景区名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getSpotList();
  },
  methods: {
    // 处理景区id到景区name的转换
    formatter(row, column) {
        for(var i of this.spotList) {
           if(i.scenicid == row.scenicid){
              return i.name
           }
        }
        return row.scenicid
      },
    /** 查询模版管理列表 */
    getList() {
      this.loading = true;
      listTemplate(this.queryParams).then(response => {
        this.templateList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询景区全部列表*/
    /** 查询景区列表 */
    getSpotList() {
      this.loading = true;
      listSpot(this.querySpotParams).then(response => {
        this.spotList = response.rows;
        this.spotTotal = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        templateName: null,
        scenicid: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加模版管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTemplate(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改模版管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTemplate(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTemplate(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    submitBatchForm(){
      console.log('sumbit me batch~')
      this.$refs["batchform"].validate(valid => {
        if (valid) {
           console.log('com on')
           console.log(this.batchform)
           this.open2 = false
        }
      });
    },
    cancelBatch()  {
      this.open2 = false;
      this.batchform = {
        newspotname: null
      };
      this.resetForm("batchform");
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除模版?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTemplate(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    handleBatchAdd(row){
      console.log('batch add')
      this.open2 = true
      this.batchform.orispotid = row.scenicid
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有模版管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTemplate(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
