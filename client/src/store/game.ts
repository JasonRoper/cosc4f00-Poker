
export default class Game {
  public hello: string = 'hhhhhhhhhhhhhhhhhh'
  constructor (h: number) {
    this.hello = 'no'
  }
  public get (): string {
    return this.hello
  }
  public set (newValue: string) {
    this.hello = newValue
  }
}
