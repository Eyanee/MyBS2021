<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="mgb20" style="height:252px;">
          <div class="user-info">
            <img src="../../assets/mark.jpeg" class="user-avator" alt>
            <div class="user-info-cont">
              <div class="user-info-name" v-text="userData.username" />
            </div>
          </div>
        </el-card>
        <el-card shadow="hover" style="height:252px;">
          <template #header>
            <el-container>
              <div class="clearfix">
                <span>个人资料</span>
              </div>
              <div class="right-items" style="float: right;">
                <el-button size="small" type="danger" plain @click="setEdit">修改</el-button>
                <el-button size="small" type="primary" plain @click="updateInfo">确认</el-button>
              </div>
            </el-container>
          </template>
          <el-container>
            <div class="Password">
              <span>昵称</span>
            </div>
            <el-col :span="15">
              <el-input v-model="userData.username" placeholder="请输入昵称" :disabled="!editable" />
            </el-col>
          </el-container>
          <!-- -->
          <el-container>
            <div class="Password">
              <span>邮箱</span>
            </div>
            <el-col :span="15">
              <el-input v-model="userData.email" placeholder="请输入邮箱" :disabled="!editable" />
            </el-col>
          </el-container>
          <!-- -->
          <el-container>
            <div class="Password">
              <span>密码</span>
            </div>
            <el-col :span="15">
              <el-input v-model="userData.password" placeholder="请输入密码" show-password :disabled="!editable" />
            </el-col>
          </el-container>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-card shadow="hover" style="height: 526px">
              <div class="grid-content grid-con-1">
                <i class="el-icon-time grid-con-icon" />
                <div class="grid-cont-right">
                  <div class="grid-num">{{ upload }}</div>
                  <div>已上传</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" style="height: 526px">
              <div class="grid-content grid-con-2">
                <i class="el-icon-star-off grid-con-icon" />
                <div class="grid-cont-right">
                  <div class="grid-num">{{ accept }}</div>
                  <div>已领取</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" style="height: 526px">
              <div class="grid-content grid-con-3">
                <i class="el-icon-bell grid-con-icon" />
                <div class="grid-cont-right">
                  <div class="grid-num">{{ release }}</div>
                  <div>已发布</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Register',
  data() {
    return {
      userData: {
        username: localStorage.getItem('username'),
        password: localStorage.getItem('password'),
        email: ''
      },
      editable: false,
      upload: 0,
      accept: 0,
      release: 0
    }
  },
  mounted: function() {
    // 获取邮箱值
    var _this = this
    var name = localStorage.getItem('username')
    axios.get('http://localhost:8080/getEmail', {
      params: {
        username: name
      }
    }).then(function(response) {
      console.log(response)
      var data = response.data
      console.log('email is', data)
      _this.userData.email = data
      console.log(_this.userData.email)
      _this.getNum()
    })
  },
  methods: {
    setEdit() {
      var _this = this
      _this.editable = true
    },
    updateInfo() {
      var _this = this
      var previousName = localStorage.getItem('username')
      axios.get('http://localhost:8080/updateInfo', {
        params: {
          UserName: _this.userData.username,
          Password: _this.userData.password,
          Email: _this.userData.email,
          PreviousName: previousName
        }
      }).then(function(response) {
        // 不管是什么，都把值设置成返回的数据
        var data = response.data
        console.log(data,'user')
        _this.userData.username = data.name
        _this.userData.password = data.password
        _this.userData.email = data.email
        _this.editable = false
      })
    },
    getNum() {
      var _this = this
      var name = localStorage.getItem('username')
      axios.get('http://localhost:8080/getNum', {
        params: {
          username: name
        }
      }).then(function(response) {
        var data = response.data
        console.log(data)
        _this.upload = data[0]
        _this.accept = data[1]
        _this.release = data[2]
      })
    }
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}
.clearfix{
  text-align: left;
  font-size: 13px;
  margin-top: 2%;
}
.right-items{
  margin-left:150px;
  font-size: 10px;
  text-align: right;
}
.Password{
  font-size: 17px;
  margin-top: 2%;
}
.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.grid-con-icon {
  font-size: 50px;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #fff;
}

.grid-con-1 .grid-con-icon {
  background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}

.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-avator {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}

.user-info-list {
  font-size: 14px;
  color: #999;
  line-height: 25px;
}

.user-info-list span {
  margin-left: 70px;
}

.mgb20 {
  margin-bottom: 20px;
}

.todo-item {
  font-size: 14px;
}

.todo-item-del {
  text-decoration: line-through;
  color: #999;
}

</style>
