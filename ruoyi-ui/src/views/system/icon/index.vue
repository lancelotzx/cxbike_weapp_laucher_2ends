<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="景区id" prop="spotId">
        <el-input
          v-model="queryParams.spotId"
          placeholder="请输入景区id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图标名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入图标名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图标链接地址" prop="url">
        <el-input
          v-model="queryParams.url"
          placeholder="请输入图标链接地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="链接类型，可能为小程序，列表，h5" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择链接类型，可能为小程序，列表，h5" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="标签，逗号分隔" prop="tags">
        <el-input
          v-model="queryParams.tags"
          placeholder="请输入标签，逗号分隔"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图标对应小程序id" prop="appid">
        <el-input
          v-model="queryParams.appid"
          placeholder="请输入图标对应小程序id"
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
          v-hasPermi="['system:icon:add']"
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
          v-hasPermi="['system:icon:edit']"
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
          v-hasPermi="['system:icon:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:icon:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="iconList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图标id" align="center" prop="id" />
      <el-table-column label="景区id" align="center" prop="spotId" />
      <el-table-column label="图标名称" align="center" prop="name" />
      <el-table-column label="图标链接地址" align="center" prop="url" />
      <el-table-column label="链接类型，可能为小程序，列表，h5" align="center" prop="type" />
      <el-table-column label="标签，逗号分隔" align="center" prop="tags" />
      <el-table-column label="图标对应小程序id" align="center" prop="appid" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:icon:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:icon:remove']"
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

    <!-- 添加或修改图标对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="景区id" prop="spotId">
          <el-input v-model="form.spotId" placeholder="请输入景区id" />
        </el-form-item>
        <el-form-item label="图标名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="图标链接地址" prop="url">
          <el-input v-model="form.url" placeholder="请输入图标链接地址" />
        </el-form-item>
        <el-form-item label="链接类型，可能为小程序，列表，h5" prop="type">
          <el-select v-model="form.type" placeholder="请选择链接类型，可能为小程序，列表，h5">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签，逗号分隔" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签，逗号分隔" />
        </el-form-item>
        <el-form-item label="图标对应小程序id" prop="appid">
          <el-input v-model="form.appid" placeholder="请输入图标对应小程序id" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listIcon, getIcon, delIcon, addIcon, updateIcon, exportIcon } from "@/api/system/icon";

export default {
  name: "Icon",
  components: {
  },
  data() {
    return {
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
      // 图标表格数据
      iconList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        spotId: null,
        name: null,
        url: null,
        type: null,
        tags: null,
        appid: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        spotId: [
          { required: true, message: "景区id不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "图标名称不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "链接类型，可能为小程序，列表，h5不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询图标列表 */
    getList() {
      this.loading = true;
      listIcon(this.queryParams).then(response => {
        this.iconList = response.rows;
        this.total = response.total;
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
        spotId: null,
        name: null,
        url: null,
        type: null,
        tags: null,
        appid: null
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
      this.title = "添加图标";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getIcon(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改图标";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateIcon(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addIcon(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除图标编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delIcon(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有图标数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportIcon(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
