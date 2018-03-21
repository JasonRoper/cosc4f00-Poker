import NavBar from '../components/NavBar'
import { mount } from 'vue-test-utils'

describe('App.test.js', () => {
  let Cmp
  beforeEach(() => {
    Cmp = mount(NavBar)
    expect(Cmp.find('.modal-footer').element).toBeInstanceOf(HTMLElement)
    // it('is it a nav component', () => {
    //   expect(Cmp.is(NavBar)).toBe(true)
    // })
  })
})
