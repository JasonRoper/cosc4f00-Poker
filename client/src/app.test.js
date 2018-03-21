import { mount } from 'vue-test-utils'
import App from './Test'

describe('test.js', () => {
  let Cmp

  beforeEach(() => {
    Cmp = mount(App, {
      data: { // Replace data value with this fake data
        messages: ['Cat']
      }
    }) // Instances and mounts the component
  })
  it('j', () => {
    expect(Cmp.vm.messages).toEqual(['Cat'])
  })
})

/*
// import NavBar from '../src/components/NavBar'
describe('App.test.js', () => {
  let Cmp
  beforeEach(() => {
    Cmp = shallow(App, {
      data: { // Replace data value with this fake data
        messages: ['Cat']
      }
      // components: {
      //   navBar: NavBar
      // }
    }) // Instances and mounts the component
  })

  it('equals messages to ["Cat"]', () => {
    expect(Cmp.vm.messages).toEqual(['Cat'])
  })

// import { mount } from 'vue-test-utils'
// import { expect } from 'chai'
// import { shallow } from '@vue/test-utils'

  beforeEach(() => {
    Cmp = shallow(App, {
      stubs: {

      }
    }
      vm.$refs)
    it('equelas messages to "CAT"', () => {
      expect(Cmp.vm.contains('div').toBe(true))
    })
  })
  */
