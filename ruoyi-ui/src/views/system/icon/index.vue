<!--
author:wangjia
description: 图标管理页面
  其中景区需要预先读取，用于景区列表
-->
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="景区" prop="scenicid">
        <el-select v-model="queryParams.scenicid" placeholder="请选择景区">
          <el-option
            v-for="item in spotList"
            :key="item.scenicid"
            :label="item.name"
            :value="item.scenicid">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图标名称" prop="iconname">
        <el-input
          v-model="queryParams.iconname"
          placeholder="请输入图标名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="链接类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择链接类型，可能为小程序，h5， 列表" clearable size="small">
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="tags">
        <el-input
          v-model="queryParams.tags"
          placeholder="请输入备注"
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
      <el-table-column label="图标id,由于排序需要，使用自增id" align="center" prop="iconid" />
      <el-table-column label="景区id，使用uuid" align="center" prop="scenicid" />
      <el-table-column label="图标名称" align="center" prop="iconname" />
      <el-table-column label="图标图片链接地址" align="center" prop="iconurl" />
      <el-table-column label="链接类型，可能为小程序，h5， 列表" align="center" prop="type" :formatter="typeFormat" />
      <el-table-column label="备注" align="center" prop="tags" />
      <el-table-column label="图标对应小程序id，当为小程序时启用" align="center" prop="appid" />
      <el-table-column label="图标对应H5网址链接名称，或小程序名称" align="center" prop="linkname" />
      <el-table-column label="图标对应h5的地址，当为h5类型时启用" align="center" prop="h5url" />
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
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="景区" prop="scenicid">
          <el-select v-model="form.scenicid" placeholder="请选择景区">
            <el-option
              v-for="item in spotList"
              :key="item.scenicid"
              :label="item.name"
              :value="item.scenicid">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图标名称" prop="iconname">
          <el-input v-model="form.iconname" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="图标图片" prop="iconurl">
          <el-input v-model="form.iconurl" placeholder="请输入图标图片链接地址" />
        </el-form-item>
        <el-form-item label="链接类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择链接类型">
            <el-option
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="图标对应小程序id" prop="appid">
          <el-input v-model="form.appid" placeholder="请输入图标对应小程序id，当为小程序时启用" />
        </el-form-item>
        <el-form-item label="图标对应链接名称、小程序名称" prop="linkname">
          <el-input v-model="form.linkname" placeholder="请输入图标对应H5网址链接名称，或小程序名称" />
        </el-form-item>
        <el-form-item label="图标对应h5或小程序的地址" prop="h5url">
          <el-input v-model="form.h5url" placeholder="请输入图标对应h5的地址，当为h5类型时启用" />
        </el-form-item>
        <el-divider content-position="center">$三级菜单信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSysLv3list">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSysLv3list">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="sysLv3listList" :row-class-name="rowSysLv3listIndex" @selection-change="handleSysLv3listSelectionChange" ref="sysLv3list">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="第三极列表子项名称" prop="name">
            <template slot-scope="scope">
              <el-input v-model="scope.row.name" placeholder="请输入第三极列表子项名称" />
            </template>
          </el-table-column>
          <el-table-column label="第三级列表子项价格" prop="price">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price" placeholder="请输入第三级列表子项价格" />
            </template>
          </el-table-column>
          <el-table-column label="第三级列表子项图片名称" prop="picname">
            <template slot-scope="scope">
              <el-input v-model="scope.row.picname" placeholder="请输入第三级列表子项图片名称" />
            </template>
          </el-table-column>
          <el-table-column label="第三级列表子项图片url" prop="picurl">
            <template slot-scope="scope">
              <el-input v-model="scope.row.picurl" placeholder="请输入第三级列表子项图片url" />
            </template>
          </el-table-column>
          <el-table-column label="第三级子项对应的链接类型，1小程序，2H5" prop="type">
            <template slot-scope="scope">
              <el-input v-model="scope.row.type" placeholder="请输入第三级子项对应的链接类型，1小程序，2H5" />
            </template>
          </el-table-column>
          <el-table-column label="子项对应小程序id，当为小程序时启用" prop="appid">
            <template slot-scope="scope">
              <el-input v-model="scope.row.appid" placeholder="请输入子项对应小程序id，当为小程序时启用" />
            </template>
          </el-table-column>
          <el-table-column label="子项对应链接名称，可以为小程序名称或H5网站名称" prop="linkname">
            <template slot-scope="scope">
              <el-input v-model="scope.row.linkname" placeholder="请输入子项对应链接名称，可以为小程序名称或H5网站名称" />
            </template>
          </el-table-column>
          <el-table-column label="子项对应h5的地址，当为h5类型时启用" prop="h5url">
            <template slot-scope="scope">
              <el-input v-model="scope.row.h5url" placeholder="请输入子项对应h5的地址，当为h5类型时启用" />
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="tags">
            <template slot-scope="scope">
              <el-input v-model="scope.row.tags" placeholder="请输入标签，逗号分隔，方便用户维护数据" />
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
import { listIcon, getIcon, delIcon, addIcon, updateIcon, exportIcon } from "@/api/system/icon";
import { listSpot } from "@/api/system/spot";
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
      // 子表选中数据
      checkedSysLv3list: [],
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
      // ${subTable.functionName}表格数据
      sysLv3listList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 链接类型，可能为小程序，h5， 列表字典
      typeOptions: [],
      // 查询全部景区用参数
      querySpotParams: {
        pageNum: 1,
        pageSize: 10000,
        name: null,
        status: null,
      },
      spotTotal: 0,
      spotList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        scenicid: null,
        iconname: null,
        iconurl: null,
        type: null,
        tags: null,
        appid: null,
        linkname: null,
        h5url: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        scenicid: [
          { required: true, message: "景区id，使用uuid不能为空", trigger: "blur" }
        ],
        iconname: [
          { required: true, message: "图标名称不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "链接类型，可能为小程序，h5， 列表不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getSpotList();
    this.getDicts("icon_link_type").then(response => {
      this.typeOptions = response.data;
    });
  },
  methods: {
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

    /** 查询图标列表 */
    getList() {
      this.loading = true;
      listIcon(this.queryParams).then(response => {
        this.iconList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 链接类型，可能为小程序，h5， 列表字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        iconid: null,
        scenicid: null,
        iconname: null,
        iconurl: null,
        type: null,
        tags: null,
        appid: null,
        linkname: null,
        h5url: null
      };
      this.sysLv3listList = [];
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
      this.ids = selection.map(item => item.iconid)
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
      const iconid = row.iconid || this.ids
      getIcon(iconid).then(response => {
        this.form = response.data;
        this.sysLv3listList = response.data.sysLv3listList;
        this.open = true;
        this.title = "修改图标";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.sysLv3listList = this.sysLv3listList;
          if (this.form.iconid != null) {
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
      const iconids = row.iconid || this.ids;
      this.$confirm('是否确认删除图标编号为"' + iconids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delIcon(iconids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
	/** ${subTable.functionName}序号 */
    rowSysLv3listIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** ${subTable.functionName}添加按钮操作 */
    handleAddSysLv3list() {
      let obj = {};
      obj.name = "";
      obj.price = "";
      obj.picname = "";
      obj.picurl = "";
      obj.type = "";
      obj.appid = "";
      obj.linkname = "";
      obj.h5url = "";
      obj.tags = "";
      this.sysLv3listList.push(obj);
    },
    /** ${subTable.functionName}删除按钮操作 */
    handleDeleteSysLv3list() {
      if (this.checkedSysLv3list.length == 0) {
        this.$alert("请先选择要删除的${subTable.functionName}数据", "提示", { confirmButtonText: "确定", });
      } else {
        this.sysLv3listList.splice(this.checkedSysLv3list[0].index - 1, 1);
      }
    },
    /** 单选框选中数据 */
    handleSysLv3listSelectionChange(selection) {
      if (selection.length > 1) {
        this.$refs.sysLv3list.clearSelection();
        this.$refs.sysLv3list.toggleRowSelection(selection.pop());
      } else {
        this.checkedSysLv3list = selection;
      }
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
