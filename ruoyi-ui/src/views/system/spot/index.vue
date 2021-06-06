<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="景区名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入景区名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="景区状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择景区状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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
          v-hasPermi="['system:spot:add']"
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
          v-hasPermi="['system:spot:edit']"
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
          v-hasPermi="['system:spot:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:spot:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="spotList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="景区id" align="center" prop="scenicid" />
      <el-table-column label="景区名称" align="center" prop="name" />
      <el-table-column label="景区状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="图标顺序，半角逗号分隔" align="center" prop="iconserial" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:spot:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:spot:remove']"
          >删除</el-button>
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

    <!-- 添加或修改景区对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="景区名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入景区名称" />
        </el-form-item>
        <el-form-item label="景区id" prop="scenicid">
          <el-input v-model="form.scenicid" :disabled="modFlag" placeholder="请输入景区id" />
        </el-form-item>
        <el-form-item label="景区状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="图标顺序" prop="iconserial">
          <el-input v-model="form.iconserial" placeholder="请输入图标顺序，半角逗号分隔" />
        </el-form-item>
        <el-divider content-position="center">图标信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSysIcon">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSysIcon">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="sysIconList" :row-class-name="rowSysIconIndex" @selection-change="handleSysIconSelectionChange" ref="sysIcon">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="景区id" prop="scenicid" v-if="false">
            <template slot-scope="scope">
              <el-input v-model="scope.row.scenicid" placeholder="请输入景区id，使用uuid" />
            </template>
          </el-table-column>
          <el-table-column label="图标名称" prop="iconname">
            <template slot-scope="scope">
              <el-input v-model="scope.row.iconname" placeholder="请输入图标名称" />
            </template>
          </el-table-column>
          <el-table-column label="图标预览" prop="iconurl">
            <template slot-scope="scope">
              <!-- <el-input v-model="scope.row.iconurl" placeholder="请输入图标图片链接地址" /> -->
              <iconAvatar :resourceObj="scope.row"  /> <!--这里把row都给到组件去-->
            </template>
          </el-table-column>
          <el-table-column label="链接类型，可能为小程序，h5， 列表" prop="type">
            <template slot-scope="scope">
              <!-- <el-input v-model="scope.row.type" placeholder="请输入链接类型，可能为小程序，h5， 列表" />
             -->
            <el-select v-model="scope.row.type" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            </template>
          </el-table-column>
          <el-table-column label="标签，逗号分隔，方便用户维护数据" prop="tags">
            <template slot-scope="scope">
              <el-input v-model="scope.row.tags" placeholder="请输入标签，逗号分隔，方便用户维护数据" />
            </template>
          </el-table-column>
          <el-table-column label="图标对应小程序id，当为小程序时启用" prop="appid">
            <template slot-scope="scope">
              <el-input v-model="scope.row.appid" placeholder="请输入图标对应小程序id，当为小程序时启用" />
            </template>
          </el-table-column>
          <el-table-column label="图标对应H5网址链接名称，或小程序名称" prop="linkname">
            <template slot-scope="scope">
              <el-input v-model="scope.row.linkname" placeholder="请输入图标对应H5网址链接名称，或小程序名称" />
            </template>
          </el-table-column>
          <el-table-column label="图标对应h5的地址，当为h5类型时启用" prop="h5url">
            <template slot-scope="scope">
              <el-input v-model="scope.row.h5url" placeholder="请输入图标对应h5的地址，当为h5类型时启用" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSpot, getSpot, delSpot, addSpot, updateSpot, exportSpot } from "@/api/system/spot";
import iconAvatar from "./iconAvatar";

export default {
  name: "Spot",
  components: { iconAvatar },
  data() {
    return {
      // icon的类型option
      options: [{
          value: '1',
          label: '小程序'
        }, {
          value: '2',
          label: 'H5页面'
        }, {
          value: '3',
          label: '三级列表'
        }],
      //新增景区标志
      addFlag: false,
      // 修改景区标志
      modFlag: false,
      // 选中修改的景区id
      tmp_scenicid: null,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSysIcon: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 景区表格数据
      spotList: [],
      // 图标表格数据
      sysIconList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 景区状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "景区名称不能为空", trigger: "blur" }
        ],
        scenicid: [
          { required: true, message: "景区id不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "景区状态不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_spot_status").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询景区列表 */
    getList() {
      this.loading = true;
      listSpot(this.queryParams).then(response => {
        this.spotList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 景区状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        scenicid: null,
        name: null,
        status: "0",
        iconserial: null
      };
      this.sysIconList = [];
      this.addFlag = false;
      this.modFlag = false;
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
      this.ids = selection.map(item => item.scenicid)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.addFlag = true;
      this.title = "添加景区";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.modFlag = true;
      this.tmp_scenicid = row.scenicid
      const scenicid = row.scenicid || this.ids
      getSpot(scenicid).then(response => {
        this.form = response.data;
        this.sysIconList = response.data.sysIconList;
        this.open = true;
        this.title = "修改景区";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.sysIconList = this.sysIconList;
          if (this.modFlag) {
            updateSpot(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else if(this.addFlag) {
            addSpot(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const scenicids = row.scenicid || this.ids;
      this.$confirm('是否确认删除景区编号为"' + scenicids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSpot(scenicids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
	/** 图标序号 */
    rowSysIconIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 图标添加按钮操作 */
    handleAddSysIcon() {
      console.log('here', this.tmp_scenicid)
      let obj = {};
      obj.scenicid = this.tmp_scenicid;
      obj.iconname = "";
      obj.iconurl = "";
      obj.type = "";
      obj.tags = "";
      obj.appid = "";
      obj.linkname = "";
      obj.h5url = "";
      this.sysIconList.push(obj);
    },
    /** 图标删除按钮操作 */
    handleDeleteSysIcon() {
      if (this.checkedSysIcon.length == 0) {
        this.$alert("请先选择要删除的图标数据", "提示", { confirmButtonText: "确定", });
      } else {
        this.sysIconList.splice(this.checkedSysIcon[0].index - 1, 1);
      }
    },
    /** 单选框选中数据 */
    handleSysIconSelectionChange(selection) {
      if (selection.length > 1) {
        this.$refs.sysIcon.clearSelection();
        this.$refs.sysIcon.toggleRowSelection(selection.pop());
      } else {
        this.checkedSysIcon = selection;
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有景区数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportSpot(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>