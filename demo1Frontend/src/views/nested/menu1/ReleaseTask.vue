<template>
  <el-table
    :data="tableData"
    border
    style="width: 100%"
  >
    <el-table-column
      fixed
      prop="date"
      label="项目名称"
      width="150"
    />
    <el-table-column
      label="项目描述"
      width="1500"
    >
      <template slot-scope="scope">
        <el-input v-show="true" v-model="scope.row.time" placeholder="请输入内容" disabled="scope.row.editable" />
        <span v-show="false">{{ scope.row.time }}</span>
      </template>
    </el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="200"
    >
      <template slot-scope="scope">
        <el-button type="primary" size="small" @click="handleClick(scope.row)">编辑</el-button>
        <el-button type="primary" size="small" @click="release(scope.row)">提交</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import axios from 'axios'

export default {

  data() {
    return {
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        province: '上海'
      }],
      editable: []
    }
  },
  methods: {
    release(row) {
      console.log(row)
      // 记得在接受数据后将数据设计成不可修改的起始值
      var _this = this
      axios.get('http://localhost:8080/getUnpost')
        .then(function(reponse) {
          var data = reponse.data
          console.log(data)
          _this.tableData = data
          for (var i = 0; i < _this.tableData.size(); i++) {
            _this.tableData[i].editable = false
          }
        })
        .catch(function(error) {
          console.log(error)
        })
    },
    Edit(row) {
      row.editable = true
    }
  }
  // mounted: function() {
  //   this.getPosts()
  // }
}
</script>
