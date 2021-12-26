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
      />
      <el-table-column
        label="图片名称"
        prop="picname"
      />
      <el-table-column
        label="所属任务"
        prop="filename"
      />
      <el-table-column
        align="right"
      >
        <template slot-scope="scope">
          <el-input v-show="true" v-model="scope.row.description" placeholder="请输入内容" v-bind:disabled="scope.row.disabled" />
          <span v-show="false">{{ scope.row.time }}</span>
        </template>
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="getxmlfile(scope.row)">下载xml</el-button>
          <el-button type="primary" size="small" @click="getcocofile(scope.row)">下载coco</el-button>
        </template>
      </el-table-column>

    </el-table>
    <el-button style="margin-left: 10px;" size="small" type="primary" @click="getPics">刷新</el-button>
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
  mounted: function() {
    this.getPics()
  },
  methods: {
    getxml(row) {
      var file = row.filename
      var user = localStorage.getItem('username')
      var picname = row.picname
      axios.get('http://localhost:8080/getxmlfile', {
        params: {
          username: user,
          filename: file,
          picname: picname
        }
      }).then(function(response) {
        const content = response.data
        console.log(content)
        const blob = new Blob([content])
        if ('download' in document.createElement('a')) {
          // 非IE下载
          console.log('enter if')
          const elink = document.createElement('a')
          elink.download = name
          elink.style.display = 'none'
          elink.href = URL.createObjectURL(blob)
          document.body.appendChild(elink)
          elink.click()
          // 释放URL对象
          URL.revokeObjectURL(elink.href)
          document.body.removeChild(elink)
        } else {
          navigator.msSaveBlob(blob, name)
        }
        // eslint-disable-next-line handle-callback-err
      }, function(err) {

      })
    },
    async getxmlfile(row) {
      console.log('row',row)
      var name = row.picname
      var filename = row.filename
      var user = localStorage.getItem('username')
      axios.defaults.baseURL = 'http://localhost:8080/'

      window.location.href = axios.defaults.baseURL + 'downloadxml/' + user + '/' + filename + '/' + name
    },
    async getcocofile(row) {
      console.log('row',row)
      var name = row.picname
      var filename = row.filename
      var user = localStorage.getItem('username')
      axios.defaults.baseURL = 'http://localhost:8080/'

      window.location.href = axios.defaults.baseURL + 'downloadcoco/' + user + '/' + filename + '/' + name
    },
    getPics() {
      var _this = this
      var name = localStorage.getItem('username')
      axios.get('http://localhost:8080/getMarkedPics', {
        params: {
          username: name
        }
      })
        .then(function(reponse) {
          var data = reponse.data
          console.log(data)
          for (var i = 0; i < data.length; i++) {
            var a = data[i].picname
            var b = data[i].filename
            var temp = { picname: a, filename: b }
            _this.tableData.push(temp)
          }
          console.log('table', _this.tableData)
        })
        .catch(function(error) {
          console.log(error)
        })
    }
  }
}
</script>
