import * as React from "react";
import { cn } from "../../lib/utils";
import { AlertTriangle, Info } from "lucide-react";

type AlertVariant = "default" | "destructive";

const icons: Record<AlertVariant, React.ReactNode> = {
  default: <Info className="h-4 w-4" />,
  destructive: <AlertTriangle className="h-4 w-4" />,
};

export interface AlertProps extends React.HTMLAttributes<HTMLDivElement> {
  variant?: AlertVariant;
}

const Alert = React.forwardRef<HTMLDivElement, AlertProps>(
  ({ className, variant = "default", children, ...props }, ref) => (
    <div
      ref={ref}
      role="alert"
      className={cn(
        "flex gap-3 rounded-lg border border-border bg-card/70 p-4 text-sm shadow-sm",
        variant === "destructive" && "border-destructive/50 bg-destructive/10 text-destructive",
        className,
      )}
      {...props}
    >
      <div className="mt-0.5 text-muted-foreground">{icons[variant]}</div>
      <div className="space-y-1">{children}</div>
    </div>
  ),
);
Alert.displayName = "Alert";

export { Alert };

