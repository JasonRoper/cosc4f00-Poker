import { shallow } from 'vue-test-utils'
import sinon from 'sinon'
import login from '@/components/Login.vue'

describe('Login.vue', () => {
  /*
  * Test will check if the name of the component is login
  */
  it('Is called login', () => {
    // Initialize the test .
    const wrapper = shallow(login)
    // Assert result
    expect(wrapper.name()).toEqual('Login')
  })
    /*
  * Test will check if the name of the component is login
  */
  it('Calls Attemptlogin() function', () => {
    // Inistilize test
    const listener = sinon.stub()
    const wrapper = shallow(login)
    wrapper.setMethods({
      AttemptLogin: listener // listens out for this method 
    })
    // click the button
    wrapper.find('.registerButton').trigger('click')
    // Assert result
    expect(listener.called).toBe(true)
  })
})
