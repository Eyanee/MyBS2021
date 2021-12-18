<template>
  <el-table
      :data="tableData"
      border
      style="width: 100%">
    <el-table-column
        fixed
        prop="date"
        label="日期"
        width="180">
    </el-table-column>
    <el-table-column
        prop="name"
        label="姓名"
        width="160">
    </el-table-column>
    <el-table-column
        prop="province"
        label="省份"
        width="150">
    </el-table-column>
    <el-table-column
        prop="city"
        label="市区"
        width="150">
    </el-table-column>
    <el-table-column
        prop="address"
        label="地址"
        width="300">
    </el-table-column>
    <el-table-column prop="zip" label="邮编" width="150">
    </el-table-column>
    <el-table-column  label="操作" width="150">
      <div class="right-items"  style="float: right;">
        <el-button size="small" type="danger" plain>修改</el-button>
        <el-button size="small" type="primary" plain>确认</el-button>
      </div>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  methods: {
    handleClick(row) {
      console.log(row);
    }
  },

  data() {
    return {
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1518 弄',
        zip: 200333
      }, {
        date: '2016-05-04',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1517 弄',
        zip: 200333
      }, {
        date: '2016-05-01',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1519 弄',
        zip: 200333
      }, {
        date: '2016-05-03',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1516 弄',
        zip: 200333
      }]
    }
  }
}
</script>
