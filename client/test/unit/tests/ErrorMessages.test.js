import { shallow } from 'vue-test-utils'
import ErrorMessages from '@/components/WebComponents/ErrorMessages.vue'
import login from '@/components/Login.vue'
/*
Test which will check that, when the prop message of the error is
being entered correctly
*/
it('Applies Error Messagge to the property', () => {
// Intialize the test
  const Error = 'Test Message'
  const wrapper = shallow(ErrorMessages, {
    propsData: {
      Error
    }
  })
// Assert restults
  expect(wrapper.text()).toBe('Error:' + Error)
}
)

/*
Test will ensure that the correct message will be displayed for
a blank input
*/
it('Applies Error Messagge to the property', () => {
// Intialize the test
  const Error = 'Both Fields Must Be Filled'
  const wrapperlogin = shallow(login)
  const wrapper = shallow(ErrorMessages)
  wrapperlogin.find('.loginButton').trigger('click')
// Assert restults
  setTimeout(() => {
    expect(wrapper.text()).toBe('Error:' + Error)
  }, 1000)
}
)
/*
Test Will use vuex store to ensure that apprpriet messages are being sent
 to the Error Message component after button click of login
*/
/*
Test Will use vuex store to ensure that apprpriet messages are being sent
 to the Error Message component after button click of Register
*/
