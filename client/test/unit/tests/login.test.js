import { shallow, createLocalVue } from 'vue-test-utils'
import Vuex from 'vuex'
import sinon from 'sinon'
import login from '@/components/Login.vue'
const localVue = createLocalVue()
localVue.use(Vuex)

describe('Login.vue', () => {
  test('Calls Attemptlogin() function', () => {
    // Inistilize test
    const listener = sinon.spy()
    const wrapper = shallow(login)
    wrapper.setMethods({
      AttemptLogin: listener // listens out for this method
    })
    // click the button
    wrapper.find('.loginButton').trigger('click')
    // Assert result expect the method to be call was it called when the button was called
    expect(listener.called).toBe(true)
  })
  /* ================================================================
    Test will check if component  function attempt Register is called
    ==================================================================
  */
  it('Calls AttemptRegister() function', () => {
    // Inistilize test
    const listener = sinon.spy()
    const wrapper = shallow(login)
    wrapper.setMethods({
      AttemptRegister: listener // listens out for this method Attempt Register
    })
    // click the button
    wrapper.find('.registerButton').trigger('click')
    // Assert result expect the method to be call was it called when the button was called
    expect(listener.called).toBe(true)
  })
    /* ================================================================
    Test will check if login fields Are empty error messages will be thrown
    ie Applies content to the opponent
    ==================================================================
  */
  it('calls errorMessage component if empty credientials', () => {
    const wrapper = shallow(login)
    wrapper.find('.loginButton').trigger('click')
    setTimeout(() => {
      expect(wrapper.contains('.error-messages')()).toBe(true)
    }, 1000)
    // Assert result expect the method to be call was it called when the button was called
  })

      /* ================================================================
    Test will check if login fields Are empty error messages will be thrown
    ie Applies content to the opponent
    ==================================================================
  */
  it('calls errorMessage component if empty credientials For registration', () => {
    const wrapper = shallow(login)
    wrapper.find('.registerButton').trigger('click')
    setTimeout(() => {
      expect(wrapper.contains('.error-messages')()).toBe(true)
    }, 1000)
    // Assert result expect the method to be call was it called when the button was called
  })
   /* ================================================================
      Test With mock data for a sucessful login and no errors
      for developer see https://markus.oberlehner.net/blog/testing-vuex-powered-vue-components-with-jest/
    ==================================================================
  */
    /* ================================================================
      Test With mock data for a sucessful Resgistration and no errors
    ==================================================================
  */
})

