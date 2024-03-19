using System;
using System.Windows.Forms;
namespace WMLSD
{
    public partial class Main : Form
    {
        public Main()
        {
            InitializeComponent();
            Global.ShowPanel = ShowPanel;
            Global.LoginButton = LoginButton;
            Global.ShowLogin();
        }
        private void LoginButton_Click(object sender, EventArgs e)
        {
            if (LoginButton.Text == "登出")
            {
                Properties.Settings.Default.AccountID = new Guid();
                Properties.Settings.Default.PageIndex = -1;
                Properties.Settings.Default.Save();
            }
            Global.ShowLogin();
        }
    }
}