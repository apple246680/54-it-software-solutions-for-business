using System;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Windows.Forms;
namespace WMLSD.UserControls
{
    public partial class Login : UserControl
    {
        public Login()
        {
            InitializeComponent(); 
        }

        private void LoginButton_Click(object sender, EventArgs e)
        {
            var entities = new Entities();
            var account = entities.Accounts.FirstOrDefault(x => x.Account1 == AccountTextBox.Text);
            if (account != null)
            {
                if (Sha256Hash(PasswordTextBox.Text) != account.Password)
                {
                    MessageBox.Show("密碼錯誤");
                    return;
                }
                if (account.Status == false)
                {
                    MessageBox.Show("此帳號已被關閉");
                    return;
                }
            }
            else
            {
                MessageBox.Show("找不到此帳號");
                return;
            }
            var bools = Properties.Settings.Default.AccountID == account.ID;
            Properties.Settings.Default.AccountID = account.ID;
            Properties.Settings.Default.Save();
            Global.ShowUserControl(new Management(bools,account.JobTitleData.JobTitle == "Visitor") { Dock = DockStyle.Fill });
            Global.LoginButton.Text = "登出";
            MessageBox.Show(account.JobTitleData.JobTitle == "Visitor"?"你是遊客":"恭喜登入成功管理員");
        }
        private string Sha256Hash(string rawData)
        {
            var sha256Hash = SHA256.Create();
            var bytes = sha256Hash.ComputeHash(Encoding.UTF8.GetBytes(rawData));
            var builder = new StringBuilder();
            for (int i = 0; i < bytes.Length; i++)
                builder.Append(bytes[i].ToString("x2"));
            return builder.ToString();
        }
    }
}
