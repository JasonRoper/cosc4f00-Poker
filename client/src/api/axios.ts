
import axiosLib from 'axios'

export default axiosLib.create({
  withCredentials: true,
  headers: {
    'X-Requested-With': 'XMLHttpRequest'
  }
})
