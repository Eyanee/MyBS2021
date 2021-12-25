<template>
  <el-table
    :data="tableData"
    border
    style="width: 100%"
  >
    <el-table-column
      fixed
      prop="filename"
      label="项目名称"
      width="150"
    />
    <el-table-column
      label="项目描述"
      width="1500"
      prop="description"
    >
      <template slot-scope="scope">
        <el-input v-show="true" v-model="scope.row.description" placeholder="请输入内容" v-bind:disabled="scope.row.disabled" />
        <span v-show="false">{{ scope.row.time }}</span>
      </template>
    </el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="200"
    >
      <template slot-scope="scope">
        <el-button type="primary" size="small" @click="Edit(scope.row)">编辑</el-button>
        <el-button type="primary" size="small" @click="release(scope.row)">提交</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import axios from 'axios'
import qs from 'qs'

export default {

  data() {
    return {
      tableData: [],
      editable: false
    }
  },
  mounted: function() {
    this.post()
    console.log('all')
  },
  methods: {
    post() {
      var _this=this;
      var user = localStorage.getItem('username')
      console.log(user)
      // 记得在接受数据后将数据设计成不可修改的起始值
      var parm = {
        username: user
      }
      console.log(parm)
      axios.get('http://localhost:8080/getUnpost', {
        params: {
          username: user
        }
      }).then(function(reponse) {
        console.log(reponse)
        var data = reponse.data
        // eslint-disable-next-line no-undef
        for (var i = 0; i < data.length; i++) {
          // _this.tableData[i].editable = false
          // eslint-disable-next-line no-undef
          var temp = { filename: data[i], description: '', disabled: true }
          _this.tableData.push(temp)
          console.log('Q', _this.tableData)
        }

      })
        .catch(function(error) {
          console.log(error)
        })
    },
    Edit(row) {
      row.disabled = false
    },
    release(row) {
      var user = localStorage.getItem('username')
      var des = row.description
      var _this=this
      console.log(des)
      this.$http.post('http://localhost:8080/release', {
        username: user,
        filename: row.filename,
        des: des
      }, { emulateJSON: true })
        .then(function(response) {
          console.log(response)
          _this.tableData=[]
          _this.post()
        })
    }
  }
}
</script>
