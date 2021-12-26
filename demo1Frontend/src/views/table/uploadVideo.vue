<template>
  <div class="video-mark-wrapper">
    <Hints>
      <template slot="hintName">视频水印</template>
      <template slot="hintInfo" />
    </Hints>
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card shadow="always">
          <div slot="header" class="title">播放区域</div>
          <div class="box-wrapper">
            <div class="drag-container">
              <video :src="videoSrc" controls />
              <ElementDrr
                v-for="(item, index) in elements"
                :key="index"
                :element="item"
                :handles="dragHandles(item.type)"
                :style="elementZIndex(item.type)"
                @updateActiveEle="updateActiveEle"
              >
                <!-- 图片 -->
                <img v-if="item.type==='image'" :src="item.src" draggable="false">
                <!-- 文字 -->
                <ImageRichText
                  v-if="item.type === 'text'"
                  v-model="item.text"
                  :element="item"
                  :active-ele-text="activeEleText"
                />
              </ElementDrr>
            </div>
          </div>
          <div class="uploadVideo">
            <el-upload
              ref="upload"
              class="upload-demo"
              action="http://localhost:8080/uploadVideo"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :file-list="fileList"
              :auto-upload="false"
            >
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
              <el-button style="margin-left: 10px;" size="small" type="primary" @click="flush">刷新</el-button>
              <div slot="tip" class="el-upload__tip">只能上传mp4文件</div>
            </el-upload>
          </div>

        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="always">
          <div slot="header" class="title">设置区域</div>
          <div>
            <el-button @click="cutPicture">
              截图
            </el-button>
            <el-button @click="uploadPics">
              上传
            </el-button>
            <el-button @click="clearPicture">
              清空
            </el-button>
          </div>
          <canvas id="myCanvas" width="343" height="200" />
          <div class="box">
            <ul class="ul">
              <li v-for="(item, i) in imgSrc" class="li">
                <img :src="item" alt="" class="img">
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>

export default {
  name: 'VideoMark',
  // eslint-disable-next-line vue/no-unused-components

  data() {
    return {
      temp: 'test.mp4',
      videoSrc: require('../../assets/video/myVideo.mp4'),
      elements: [], // 叠加组件数组
      activeEle: {}, // 当前图片上聚焦的叠加组件
      eleNum: 0,
      imgSrc: [],
      videoname: 'myVideo.mp4'
    }
  },
  computed: {
    // 选择的文本
    // eslint-disable-next-line vue/return-in-computed-property
    activeEleText() {
      if (this.activeEle.type === 'text') {
        return this.activeEle
      }
    }
  },
  created() {
    this.addText()
    this.addImage({
      active: false,
      size: 53248,
      width: 132,
      height: 132
    })
  },
  methods: {
    // 拖动元素把柄
    dragHandles(type) {
      if (type === 'text') {
        return ['e', 'w']
      }
    },
    uploadPics() {
      const name = localStorage.getItem('username')
      this.$http.post('http://localhost:8080/videoPics', {
        base64Str: this.imgSrc,
        username: name,
        videoname: this.videoname
      }, { emulateJSON: true }
      ).then(function(response) {
        console.log(response)
      })
    },
    // 元素层级排序
    submitUpload() {
      var s = this.$refs.upload.uploadFiles[0].raw
      console.log(s,'s')
      console.log(this.$refs.upload.uploadFiles,'here')
      var filereader=new FileReader()
      filereader.readAsDataURL(s)
      var _this=this
      filereader.onload=function(e){
        _this.videoSrc=e.currentTarget.result
        console.log(_this.videoSrc)
      }

      // console.log(this.imgSrc)
      this.videoname = s.name
      console.log('vname', s.name)
    },
    flush() {
      this.videoSrc = require('../../assets/video/' + this.temp)
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handlePreview(file) {
      console.log(file)
    },
    elementZIndex(type) {
      let zIndex = 1
      switch (type) {
        case 'text':
          zIndex = 3
          break
        case 'image':
          zIndex = 2
          break
      }
      return {
        zIndex: zIndex
      }
    },
    // 添加文本
    addText() {
      const tagNum = ++this.eleNum
      const text = {
        active: true,
        text: '请输入文本',
        type: 'text',
        tag: 'text_' + tagNum,
        x: 300,
        y: 80,
        w: 180,
        h: 36,
        style: {
          textAlign: 'left',
          lineHeight: '24px',
          fontSize: '24px',
          fontFamily: '微软黑体',
          fontWeight: 400,
          color: '#687684',
          backgroundColor: '#9ac8d8',
          overflow: 'hidden'
        }
      }
      if (this.elements.length > 4) {
        this.$message.warning('图片上最多叠加5个元素！')
      } else {
        this.elements.push(text)
        this.updateActiveEle(text)
      }
    },
    // 添加图片
    addImage(imgObj) {
      const tagNum = ++this.eleNum
      const image = {
        active: imgObj.active,
        type: 'image',
        tag: 'image_' + tagNum,
        x: 320,
        y: 260,
        w: parseInt(imgObj.width),
        h: parseInt(imgObj.height),
        src: imgObj.src,
        size: imgObj.size
      }
      if (this.elements.length > 4) {
        this.$message.warning('图片上最多叠加5个元素！')
      } else {
        this.elements.push(image)
        if (imgObj.active) {
          this.updateActiveEle(image)
        }
      }
    },
    // 更新当前选中的元素
    updateActiveEle(element) {
      this.activeEle = element
    },
    // 删除图片上当前选择的元素
    deleteActiveEle() {
      const newElements = this.elements.filter(item => {
        return item.tag !== this.activeEle.tag
      })
      this.$nextTick(() => {
        this.elements = newElements
      })
      this.updateActiveEle({})
    },
    cutPicture() {
      var v = document.querySelector('video')
      const canvas = document.getElementById('myCanvas')
      var ctx = canvas.getContext('2d')
      ctx.drawImage(v, 0, 0, 343, 200)
      var oGrayImg = canvas.toDataURL('image/jpeg')
      // this.imgSrc = oGrayImg
      this.imgSrc.push(oGrayImg)
    },
    clearPicture() {
      this.imgSrc = []
    }
  }
}
</script>

<style lang="scss">
.video-mark-wrapper {
  .box-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 550px;
    overflow: hidden;

    .drag-container {
      position: relative;
      width: 850px;
      height: 478px;

      video {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
      }

      .z-drr-container {
        border: 1px dashed transparent;

        &.z-active {
          border: 1px dashed #2e95ff;
        }
      }
    }
  }
}

  .box-content {
    height: 550px;
    overflow: hidden;

    .form-wrapper {
      width: 300px;
      margin: 50px auto 0;
    }

    .el-button {
      width: 210px;
    }
  }

  .box {
    //margin-top: 10px;
    .ul {
      display: flex;
      flex-wrap: wrap;
      position: relative;
      margin-top: 10px
    }

    .li {
      padding: 3px;
      list-style: none;
      margin-right: 15px;
    }

    .img {
      width: 200px;
      height: 150px
    }
  }
  .uploadVideo{
    margin-left: 100px;
  }

</style>
