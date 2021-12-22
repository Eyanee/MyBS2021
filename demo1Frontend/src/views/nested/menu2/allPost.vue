<template>
  <div>
    <el-table
      :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
      style="width: 100%"
    >
      <el-table-column
        label="序号"
        type="index"
        width="50"
      >
      </el-table-column>
      <el-table-column
        label="项目名称"
        prop="filename"
      >
      </el-table-column>
      <el-table-column
        label="项目信息"
        prop="desInfo"
      >
      </el-table-column>
      <el-table-column
        label="发布人"
        prop="username"
      >
      </el-table-column>
      <el-table-column
        align="right"
      >
        <template slot="header" slot-scope="scope">
          <el-input
            v-model="search"
            size="mini"
            placeholder="输入关键字搜索"
          />
        </template>
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleReceive(scope.$index, scope.row)"
          >领取
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button style="margin-left: 10px;" size="small" type="primary" @click="getPosts">刷新</el-button>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      tableData: [],
      search: ''
    }
  },
  methods: {
    handleReceive(index, row) {
      console.log(index, row)
      console.log(this.tableData[index])// 先看看可不可以输出
      var temp = this.tableData[index]
      this.$http.post('http://localhost:8080/setReceive', {
        'username': temp.username,
        'filename': temp.filename,
        'receivePerson': localStorage.getItem('username')
      })
        .then(function(response) {
          console.log(response.data)
        })
      this.reload()
    },
    getPosts() {
      var _this = this
      axios.get('http://localhost:8080/getAllPost')
        .then(function(reponse) {
          var data = reponse.data
          console.log(data)
          _this.tableData = data
        })
        .catch(function(error) {
          console.log(error)
        })
    }
  },
  mounted: function() {
    this.getPosts()
  }
}
</script>
