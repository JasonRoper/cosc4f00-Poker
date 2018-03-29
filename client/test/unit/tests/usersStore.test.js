import users from '@/store/users'

describe('user mutations should have the correct effect', () => {
  let store

  beforeEach(() => {
    store = Object.assign({}, users.state)
  })
  test('logout should remove all user identifying information', () => {
    users.mutations.logout(store)

    expect(store.username).toBe(null)
    expect(store.userId).toBe(null)
    expect(users.getters.loggedIn(store)).toBe(false)
  })

  test('login should set all user identifying information', () => {
    users.mutations.login(store, {
      username: 'jason',
      userId: 12
    })

    expect(store.username).toBe('jason')
    expect(store.userId).toBe(12)
    expect(users.getters.loggedIn(users)).toBe(true)
  })

  test('addLoginError should add an error to the login section', () => {
    let error = {
      error: 'AuthenticationError',
      message: 'Error Authenticating'
    }
    users.mutations.addLoginError(store, error)

    expect(store.errors.login.length()).toBe(1)
    expect(store.errors.login[0]).toEqual(error)
    expect(store.state).toBe(false)
  })

  test('addRegistrationError should add an error to the registration section', () => {
    let error = {
      field: 'email',
      message: 'a message'
    }
    users.mutations.addRegistrationError(store, error)

    expect(store.errors.registration.length()).toBe(1)
    expect(store.errors.registration[0]).toEqual(error)
    expect(store.state).toBe(false)
  })
})

describe('login should attempt to login and set state', () => {
  let initialState

  beforeEach(() => {
    initialState = users.state
  })

  test('login successful', () => {

  })

  test('login failed due to invalid parameters', () => {
    const error = {
      error: 'AuthenticationError',
      message: 'Error Authenticating'
    }

    users.axios = {
      get (path, headers) {
        return null
      }
    }

    users.actions.login({
      username: 'jason',
      password: 'badPassword'
    }).then(() => {
      const loginErr = users.state.errors.loginz

      // The error was added
      expect(loginErr.length).toBeGreaterThan(0)

      // The error that was added is correct
      expect(loginErr).toContainEqual(error)

      // when the error occurs, it doesn't modify the
      // rest of the user state
      users.errors.login = initialState.errors.login
      expect(users).toEqual(initialState)
    })
  })
})
