<template>
  <div>
    <el-card shadow="hover" class="mgb20" style="height:1000px;">
      <div class="user-info">
        <el-upload
          ref="upload"
          class="upload-demo"
          action="http://localhost:8080/uploadPics"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          :auto-upload="false"
          :data="{username:multipartFile.username,filename:multipartFile.filefolder}"
        >
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
          <div slot="tip" class="el-upload__tip">单次只能上传一个jpg/png文件</div>
        </el-upload>
      </div>
      <el-container class="PicInfo">
        <el-col :span="2" class="file_tips">
          请输入需要上传的项目目录
        </el-col>
        <el-col :span="5" class="file">
          <el-input v-model="multipartFile.filefolder" placeholder="请输入项目名" :disabled="false" />
        </el-col>
        <el-col :span="5" class="tip">
        <div v-show="is_show"> 上传失败</div>
        </el-col>
      </el-container>

      <el-carousel :interval="4000" type="card" height="300px" class="picShow">
        <el-carousel-item v-for="item in picInfo" :key="item" :label="item.codenum">
          <img :src="item.baseStr" class="img">
        </el-carousel-item>
      </el-carousel>

      <el-container class="picNum">
        <el-col :span="2" class="file_tips">
          请输入需要查看的文件目录
        </el-col>
        <el-col :span="5" class="file">
          <el-input v-model="filename" placeholder="请输入文件名" :disabled="false" />
        </el-col>
        <el-col :span="5" class="file">
          <el-button size="small" type="primary" @click="getFilePic">查看</el-button>
        </el-col>
      </el-container>
      <el-container class="picNum">
        <el-col :span="2" class="file_tips">
          请输入需要删除的图片序号
        </el-col>
        <el-col :span="5" class="file">
          <el-input v-model="picnum" placeholder="请输入序号" :disabled="false" />
        </el-col>
        <el-col :span="5" class="file">
          <el-button size="small" type="primary" @click="deletePic">删除</el-button>
        </el-col>
      </el-container>

    </el-card>
  </div>
</template>
<script>
import axios from 'axios'
import qs from 'qs'
export default {
  data() {
    return {
      is_show: false,
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      fileList: [],
      multipartFile: { username: localStorage.getItem('username'), filefolder: 'default' },
      videoInfo: { username: 'default', filefolder: 'default' },
      picInfo: [], // picname,base64字符串,username,filename,codenum
      picnum: 0,
      filename: ''
    }
  },
  methods: {
    submitUpload() {
      var t = localStorage.getItem('username')
      console.log(this.multipartFile.username)
      var m=this.multipartFile.filefolder
      if (m === null || m === '') {
        this.is_show=true;
        return;
      }
      this.is_show=false;
      this.$refs.upload.submit()
    },
    deletePic() {
      var temp = this.picnum
      console.log(temp)
      if (temp >= this.picInfo.length){
        return
      }
      var _this = this
      var picname = _this.picInfo[temp].picname
      console.log(picname,'picname')
      var filename = _this.picInfo[temp].filename
      console.log(filename,'filename')
      var username = localStorage.getItem('username')
      console.log(username,'username')
      _this.$http.post('http://localhost:8080/deletePic', {
        picname: picname,
        filename: filename,
        username: username
      },{ emulateJSON: true }).then(function(response){
        var data=response.data
        console.log(data)
        _this.getFilePic()
      })
    },
    handlePreview(file) {
      console.log(file)
    },
    handleRemove(file) {
      console.log(file)
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleDownload(file) {
      console.log(file.raw)
    },
    uploadFile() {
      console.log('111')
      const fd = new FormData()

      for (var i = 0; i < this.$refs.upload.uploadFiles.length; i++) {
        console.log(this.$refs.upload.uploadFiles[i])
        fd.set('filename', this.$refs.upload.uploadFiles[i].raw)
        this.$http.post('http://localhost:8080/uploadPics', fd)
          .then(function(res) {
            console.log('ok')
          })
      }
    },
    getFilePic() {
      var _this = this
      var user = localStorage.getItem('username')
      axios.get('http://localhost:8080/getFilePicsData', {
        params: {
          username: user,
          filename: this.filename
        }
      }).then(function(response) {
        console.log(response.data)
        var data = response.data
        var file = _this.filename
        var str
        _this.picInfo=[]
        for (var i = 0; i < data.length; i++) {
          str = data[i].base64str
          console.log(str)
          var temp = { picname: data[i].filename, baseStr: str, filename: file, codenum: i }
          _this.picInfo.push(temp)
        }
        console.log('picInfo', _this.picInfo)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.user-info{
  margin-top: 100px;
}
.FileName{
  font-size: 0px;
}
.PicInfo{
  margin-top: 40px;
}
.file{
  margin-left: 30px;
}
.pic_tip{
  margin-bottom: 20px;
}
.file_tips{
  font-size: 15px;
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
.picShow{
  margin-top: 60px;
}
.img{
  /*设置图片宽度和浏览器宽度一致*/
  width: 100%;
  height: inherit;
}
.tip{
  margin-left: 30px;
  margin-top: 10px;
  color: red;
}

</style>
