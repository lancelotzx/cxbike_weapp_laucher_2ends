<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="第二级图标的id" prop="iconid">
        <el-input
          v-model="queryParams.iconid"
          placeholder="请输入第二级图标的id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="第三极列表子项名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入第三极列表子项名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="三级子项链接类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择第三级子项对应的链接类型，1小程序，2H5" clearable size="small">
          <el-option
            v-for="dict in typeOptions"
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
          v-hasPermi="['system:lv3list:add']"
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
          v-hasPermi="['system:lv3list:edit']"
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
          v-hasPermi="['system:lv3list:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:lv3list:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lv3listList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="第二级图标id" align="center" prop="iconid" />
      <el-table-column label="第三极列表子项名称" align="center" prop="name" />
      <el-table-column label="第三级列表子项价格" align="center" prop="price" />
      <el-table-column label="第三级列表子项图片预览" align="center" prop="picurl" >
        <template slot-scope="scope">
              <!-- <el-input v-model="scope.row.iconurl" placeholder="请输入图标图片链接地址" /> -->
              <iconAvatar :iconurl="scope.row.picurl"  /> <!--这里把row都给到组件去-->
            </template>
      </el-table-column>
      <el-table-column label="第三级子项对应的链接类型，1小程序，2H5" align="center" prop="type" :formatter="typeFormat" />
      <el-table-column label="子项对应小程序id，当为小程序时启用" align="center" prop="appid" />
      <el-table-column label="标签,维护数据" align="center" prop="tags" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lv3list:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lv3list:remove']"
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

    <!-- 添加或修改三级列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="二级图标id" prop="iconid">
          <el-input v-model="form.iconid" placeholder="请输入第二级图标的id" />
        </el-form-item>
        <el-form-item label="第三级子项名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入第三极列表子项名称" />
        </el-form-item>
        <el-form-item label="三级子项价格" prop="price">
          <el-input v-model="form.price" placeholder="请输入第三级列表子项价格" />
        </el-form-item>
        <el-form-item label="三级子项图片名称" prop="picname">
          <el-input v-model="form.picname" placeholder="请输入第三级列表子项图片名称" />
        </el-form-item>
        <el-form-item label="三级图片url" prop="picurl">
          <!-- <el-input v-model="form.picurl" placeholder="请输入第三级列表子项图片url" /> -->
          <iconAvatar :iconurl="iconurl" @iconURL="getIconURL" />
        </el-form-item>
        <el-form-item label="三级子项链接类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择第三级子项对应的链接类型，1小程序，2H5">
            <el-option
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="子项对应小程序id" prop="appid">
          <el-input v-model="form.appid" placeholder="请输入子项对应小程序id，当为小程序时启用" />
        </el-form-item>
        <el-form-item label="子项链接" prop="h5url">
          <el-input v-model="form.h5url" placeholder="请输入子项对应h5的地址，当为h5类型时启用" />
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签，逗号分隔，方便用户维护数据" />
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
import { listLv3list, getLv3list, delLv3list, addLv3list, updateLv3list, exportLv3list } from "@/api/system/lv3list";
import iconAvatar from "../icon/iconAvatar";
export default {
  name: "Lv3list",
  components: {
    iconAvatar
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
      // 三级列表表格数据
      lv3listList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 第三级子项对应的链接类型，1小程序，2H5字典
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        iconid: null,
        name: null,
        price: null,
        picname: null,
        picurl: null,
        type: null,
        appid: null,
        linkname: null,
        h5url: null,
        tags: null
      },
      // 表单参数
      form: {},
      iconurl: '',
      // 表单校验
      rules: {
        iconid: [
          { required: true, message: "第二级图标的id不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "第三极列表子项名称不能为空", trigger: "blur" }
        ],
        price: [
          { required: true, message: "第三级列表子项价格不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "第三级子项对应的链接类型，1小程序，2H5不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("lv3_linke_type").then(response => {
      this.typeOptions = response.data;
    });
  },
  methods: {
    getIconURL(url){
      this.form.picurl = url;
    },
    /** 查询三级列表列表 */
    getList() {
      this.loading = true;
      listLv3list(this.queryParams).then(response => {
        this.lv3listList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 第三级子项对应的链接类型，1小程序，2H5字典翻译
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
        id: null,
        iconid: null,
        name: null,
        price: null,
        picname: null,
        picurl: null,
        type: null,
        appid: null,
        linkname: null,
        h5url: null,
        tags: null
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
      this.iconurl = '';
      this.open = true;
      this.title = "添加三级列表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.iconurl = row.picurl;
      console.log('update', this.iconurl)
      const id = row.id || this.ids
      getLv3list(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改三级列表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLv3list(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLv3list(this.form).then(response => {
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
      this.$confirm('是否确认删除三级列表编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delLv3list(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有三级列表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportLv3list(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
