/* spms-nav.js — injects the sidebar into every page */
(function () {
  var currentPage = location.pathname.split('/').pop() || 'login.html';

  var nav = [
    {
      label: 'Account',
      links: [
        { href: 'login.html',   label: 'Login',  icon: '<path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"/><polyline points="10 17 15 12 10 7"/><line x1="15" y1="12" x2="3" y2="12"/>' }
      ]
    },
    {
      label: 'Employees',
      links: [
        { href: 'addEmployee.html',    label: 'Add Employee',    icon: '<path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><line x1="19" y1="8" x2="19" y2="14"/><line x1="22" y1="11" x2="16" y2="11"/>' },
        { href: 'viewEmployee.html',   label: 'View Employees',  icon: '<path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/>' },
        { href: 'updateEmployee.html', label: 'Update Employee', icon: '<path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>' },
        { href: 'deleteEmployee.html', label: 'Delete Employee', icon: '<polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/><path d="M10 11v6"/><path d="M14 11v6"/><path d="M9 6V4h6v2"/>' }
      ]
    },
    {
      label: 'Projects',
      links: [
        { href: 'addProject.html',  label: 'Add Project',   icon: '<rect x="3" y="3" width="18" height="18" rx="2"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/>' },
        { href: 'viewProject.html', label: 'View Projects', icon: '<rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>' }
      ]
    },
    {
      label: 'Assignments',
      links: [
        { href: 'assignProject.html', label: 'Assign Project', icon: '<polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/>' }
      ]
    }
  ];

  var svgWrap = function (d) {
    return '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">' + d + '</svg>';
  };

  var html = '<div class="sidebar">'
    + '<div class="sidebar-logo">'
    + '<div class="logo-icon">' + svgWrap('<rect x="2" y="3" width="20" height="14" rx="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/>') + '</div>'
    + '<div class="logo-title">SPMS</div>'
    + '<div class="logo-sub">Personnel Management</div>'
    + '</div>'
    + '<div class="nav-section">';

  nav.forEach(function (section) {
    html += '<div class="nav-label">' + section.label + '</div>';
    section.links.forEach(function (link) {
      var active = currentPage === link.href ? ' active' : '';
      html += '<a href="' + link.href + '" class="nav-link' + active + '">'
            + svgWrap(link.icon)
            + '<span>' + link.label + '</span>'
            + '</a>';
    });
  });

  html += '</div>'
        + '<div class="sidebar-footer">v1.0 &nbsp;·&nbsp; spms_db</div>'
        + '</div>';

  document.body.insertAdjacentHTML('afterbegin', html);
})();
