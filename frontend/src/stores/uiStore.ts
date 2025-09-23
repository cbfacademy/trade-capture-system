import { makeAutoObservable } from 'mobx';

class UIStore {
  isSidebarOpen = true;
  notification = null;
  modal = null;

  constructor() {
    makeAutoObservable(this);
  }

  setSidebarOpen(isOpen) {
    this.isSidebarOpen = isOpen;
  }

  showNotification(message) {
    this.notification = message;
    setTimeout(() => {
      this.notification = null;
    }, 3000);
  }

  showModal(modalContent) {
    this.modal = modalContent;
  }

  closeModal() {
    this.modal = null;
  }
}

const uiStore = new UIStore();
export default uiStore;

