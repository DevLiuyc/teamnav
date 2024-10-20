new Vue({
    el: '#app',
    data() {
        return {
            isDarkMode: false,
            keywords: '',
            isCollapsed: localStorage.IndexCollapsed === 'true',
            datas: [],
            users: []
        }
    },
    mounted() {
        if (localStorage.username
            && localStorage.password
            && localStorage.autoLogin === 'true') {
            this.$http.post('/api/v1/quick/login', {
                username: localStorage.username, password: localStorage.password
            }).then(res => {
                if (res.data) {
                    this.loadTree();
                }
            })
        }
        this.loadTree();
        // iview的backtop组件要求高度固定，只能重写逻辑
        document.querySelector('.main-content')
            .addEventListener('scroll', this.handleScroll)
        // 如果用户有保存的主题偏好，恢复用户的选择
        const savedTheme = localStorage.getItem('darkMode');
        if (savedTheme) {
            this.isDarkMode = savedTheme === 'true';
        }
    },
    computed: {
        isSmallLayout() {
            if (!localStorage.layoutSize) {
                localStorage.layoutSize = defLayoutSize;
            }
            return localStorage.layoutSize === 'small-layout'
        },
        rotateIcon() {
            return [
                'menu-icon',
                this.isCollapsed ? 'rotate-icon' : ''
            ];
        },
        menuitemClasses() {
            return [
                'menu-item',
                this.isCollapsed ? 'collapsed-menu' : ''
            ]
        }
    },
    methods: {
        toggleDarkMode() {
            this.isDarkMode = !this.isDarkMode;
            // 可以通过 localStorage 保存用户的偏好
            localStorage.setItem('darkMode', this.isDarkMode);
        },
        handleScroll(e) {
            document.querySelector('.ivu-back-top').style.display
                = e.target.scrollTop > 400 ? 'block' : 'none';
        },
        scrollToTop() {
            document.querySelector('.main-content div').scrollIntoView(
                {behavior: 'smooth', block: 'start'})
        },
        loadTree() {
            this.$http.get(`/api/v1/card/tree?keywords=${encodeURIComponent(this.keywords)}`)
                .then(res => {
                    this.datas = res.data;
                })
                .catch(err => {
                    console.error(err);
                    this.$notice.err(err.response.data);
                })
        },
        collapsedSider() {
            this.$refs.sidebar.toggleCollapse();
            localStorage.IndexCollapsed = this.isCollapsed;
        },
        choosedMenu(id) {
            const rows = document.getElementById(id);
            rows.scrollIntoView(
                {behavior: 'smooth', block: 'start'});
            setTimeout(() => {
                if (rows.getBoundingClientRect().top > 100) {
                    rows.classList.add('blink-box');
                    setTimeout(() => {
                        rows.classList.remove('blink-box');
                    }, 3200);
                }
            }, 500);

        },
        dropdownClick(id) {
            this.choosedMenu(id);
        },
        gotoAdmin() {
            location.href = '/admin/category';
        },
    }
});
