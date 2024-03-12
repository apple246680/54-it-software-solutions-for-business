using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using WMLSD.UserControls;
namespace WMLSD
{
    public static class Global
    {
        public static Panel ShowPanel { get; set; }
        public static Button LoginButton { get; set; }
        public static void ShowUserControl(Control control)
        {
            ShowPanel.Controls.Clear();
            ShowPanel.Controls.Add(control);
        }
        public static void ShowLogin()
        {
            LoginButton.Text = "登入";
            ShowUserControl(new Login {Dock=DockStyle.Fill});
        }
    }
}
